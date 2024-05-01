import soot.*;
import soot.jimple.*;
import soot.options.Options;
import soot.util.*;
import soot.jimple.internal.*;

import java.util.*;
import java.lang.Object;
import java.io.*;

public class MergeNullChecks {

    public static void main(String[] args) {
        String classpath = ".";
        String className = "RedundantNullChecks";
        String[] sootArgs={
            "-pp",
            "-f","c",
            "-keep-line-number",
            "-main-class", className,
             className,
             "-print-tags",
            };
        // Set up Soot
        Options.v().set_soot_classpath(classpath + ":" + System.getProperty("java.home") + "/lib/rt.jar");
        SootClass sc = Scene.v().loadClassAndSupport(className);
        sc.setApplicationClass();
        Scene.v().loadNecessaryClasses();

        // Run analysis and transformation
        PackManager.v().getPack("jtp").add(new Transform("jtp.mergeNullChecks", new BodyTransformer() {
            @Override
            protected void internalTransform(Body body, String phase, Map<String, String> options) {
                List<Unit> unitsToRemove = new ArrayList<>();
                Unit lastNonNullCheck = null;

                for (Unit u : body.getUnits()) {
                    if (u instanceof JIfStmt) {
                        JIfStmt ifStmt = (JIfStmt) u;
                        Value condition = ifStmt.getCondition();

                        // Check if the condition is a null check
                        if (condition instanceof EqExpr || condition instanceof NeExpr) {
                            Value op1 = ((BinopExpr) condition).getOp1();
                            Value op2 = ((BinopExpr) condition).getOp2();

                            // Check if the null check is redundant
                            if ((op1 instanceof NullConstant && !(op2 instanceof NullConstant))
                                    || (!(op1 instanceof NullConstant) && op2 instanceof NullConstant)) {
                                // If it's the first redundant null check, store it
                                if (lastNonNullCheck == null) {
                                    lastNonNullCheck = ifStmt;
                                } else {
                                    // Remove redundant null check
                                    unitsToRemove.add(ifStmt);
                                }
                            } else {
                                // If it's not a redundant null check, reset lastNonNullCheck
                                lastNonNullCheck = null;
                            }
                        }
                    }
                }

                // Remove redundant null checks
                for (Unit u : unitsToRemove) {
                    body.getUnits().remove(u);
                }
            }
        }));

        // Output Jimple code
        // Options.v().set_output_format(Options.output_format_jimple);
        Options.v().set_output_dir("sootOutput/transformed");
        soot.Main.main(sootArgs);
    }
}
