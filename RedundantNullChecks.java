import java.util.Random;

public class RedundantNullChecks {
    public static void main(String[] args) {
        long startTime = System.nanoTime();
        int a = 0;
        for (int i=0; i<99999999; i++){
            Random random = new Random();
            int randomNumber = random.nextInt(20); // Random number between 0 and 19
            Object obj;
            if (randomNumber < 10) {
                obj = null;
            } else {
                obj = new Object();
            }
            
            if (obj == null) {
                a+=1;
                continue;
            } else {
                a-=1;
            }
            
            // Redundant null checks
            // If obj == null, code would not reach here
            if (obj != null) {
                a-=1;
            }

            if (obj != null) {
                a-=1;
            }
        }
        System.out.println("value of a:"+ a);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        System.out.println("Execution time: " + duration + " milliseconds");
    }
}
