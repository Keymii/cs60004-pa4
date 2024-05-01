import soot.*;
import soot.jimple.*;
import soot.options.Options;
import soot.util.*;
import soot.jimple.internal.*;

import java.util.*;
import java.lang.Object;

public class NullCheckAnalysis {

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

        // Run analysis1
        PackManager.v().getPack("jtp").add(new Transform("jtp.myTransform", new BodyTransformer() {
            @Override
            protected void internalTransform(Body body, String phase, Map<String, String> options) {
                for (Unit u : body.getUnits()) {
                    // System.out.println(u);
                    if (u instanceof JIfStmt) {
                        // System.out.println('b');
                        JIfStmt ifStmt = (JIfStmt) u;
                        Value condition = ifStmt.getCondition();
                        if (condition instanceof EqExpr || condition instanceof NeExpr) {
                            Value op1 = ((BinopExpr) condition).getOp1();
                            Value op2 = ((BinopExpr) condition).getOp2();
                            if ((op1 instanceof NullConstant && !(op2 instanceof NullConstant))
                                    || (!(op1 instanceof NullConstant) && op2 instanceof NullConstant)) {
                                System.out.println("Redundant null check found at line: " + ifStmt.getJavaSourceStartLineNumber());
                            }
                        }
                    }
                }
            }
        }));

        // Run Soot
        soot.Main.main(sootArgs);
    }
}
