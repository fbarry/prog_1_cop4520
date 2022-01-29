import java.util.Arrays;

public class MainThread {

    public static int MAX = 100_000_001, SQRT_MAX = 10_001, NUM_THREADS = 8;

    public static boolean[] isPrime = new boolean[MAX];
    public static long totalSum = 0;
    public static int totalNum = 0;
    
    public static void main(String[] args) throws Exception {

        int sections = (MAX + NUM_THREADS - 1) / NUM_THREADS;
        Arrays.fill(isPrime, true);
        isPrime[0] = isPrime[1] = false;
        Thread[] threads = new Thread[NUM_THREADS];

        long clock = System.currentTimeMillis();

        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(new SieveThread(sections * i, sections * (i + 1)));
            threads[i].start();
        }

        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i].join();
        }

        clock = System.currentTimeMillis() - clock;

        int[] topTen = new int[10];
        
        for (int i = MAX - 2; i >= 3; i -= 2) {
            if (isPrime[i]) {
                totalSum += i;
                totalNum++;
                if (totalNum <= 10)
                    topTen[10 - totalNum] = i;
            }
        }
        
        totalSum += 2;
        totalNum++;

        System.out.println(clock + " " + totalNum + " " + totalSum);
        for (int i = 0; i < 10; i++)
            System.out.print(topTen[i] + " ");
        System.out.println();
    }

    static class SieveThread implements Runnable {
        private int lo, hi;
    
        public SieveThread(int lo, int hi) {
            this.lo = lo;
            this.hi = Math.min(hi, MAX);
        }
    
        public void run() {
            for (int i = 3; (long) i < SQRT_MAX; i += 2) {
    
                int temp = i * i;
                if (temp < lo)
                    temp = ((lo + i - 1) / i) * i;
                
                for (int j = temp; j < hi; j += i) {
                    isPrime[j] = false;
                }
            }
        }
    }
}
