import java.util.Random;

public class RedundantNullChecks {
    public static void main(String[] args) {
        long startTime = System.nanoTime();
        int a = 0;
        for (int i=0; i<100000; i++){
            Random random = new Random();
            int randomNumber = random.nextInt(20); // Random number between 0 and 19
            // System.out.println(randomNumber);
            Object obj;
            if (randomNumber < 10) {
                obj = null;
            } else {
                obj = new Object();
            }
            
            if (obj == null) {
                // System.out.println("Object is null");
                a+=1;
                continue;
            } else {
                // System.out.println("Object is not null");
                a-=1;
            }
            
            // Redundant null checks
            // If obj == null, code would not reach here
            if (obj != null) {
                // System.out.println("Object is not null");
                a-=1;
            }

            if (obj != null) {
                // System.out.println("Object is not null");
                a-=1;
            }
        }
        System.out.println("value of a:"+ a);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        System.out.println("Execution time: " + duration + " milliseconds");
    }
}
