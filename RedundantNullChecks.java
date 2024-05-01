import java.util.Random;

public class RedundantNullChecks {
    public static void main(String[] args) {
        long startTime = System.nanoTime();
        for (int i=0; i<1000; i++){
            Random random = new Random();
            int randomNumber = random.nextInt(20); // Random number between 0 and 19
            System.out.println(randomNumber);
            Object obj;
            if (randomNumber < 10) {
                obj = null;
            } else {
                obj = new Object();
            }
            
            if (obj == null) {
                System.out.println("Object is null");
                continue;
            } else {
                System.out.println("Object is not null");
            }
            
            // Redundant null checks
            // If obj == null, code would not reach here
            if (obj != null) {
                System.out.println("Object is not null");
            }

            if (obj != null) {
                System.out.println("Object is not null");
            }
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        System.out.println("Execution time: " + duration + " milliseconds");
    }
}
