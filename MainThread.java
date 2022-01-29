/*

Fiona Barry
01/28/2022
COP 4520
Programming Assignment 1: Parallel Prime Sieve

I chose to implement the Sieve of Eratosthenes because (frankly) it was the
easiest/most intutive to understand and parallelize.

Each sieve processes one section. The Sieve of Eratosthenes is useful for
finding primes in a range of numbers, so I take advantage of that.

*/

import java.util.Arrays;

public class MainThread {

    // Project provided specifications
    public static int MAX = 100_000_001, SQRT_MAX = 10_001, NUM_THREADS = 8;

    // Results
    public static boolean[] isPrime = new boolean[MAX];
    public static long totalSum = 0;
    public static int totalNum = 0;
    
    public static void main(String[] args) throws Exception {

        // Size of each section
        int sections = (MAX + NUM_THREADS - 1) / NUM_THREADS;

        // Initialize isPrime
        Arrays.fill(isPrime, true);
        isPrime[0] = isPrime[1] = false;

        // Initialize thread array
        Thread[] threads = new Thread[NUM_THREADS];

        // Start the clock
        long clock = System.currentTimeMillis();

        // Start each thread
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new Thread(new SieveThread(sections * i, sections * (i + 1)));
            threads[i].start();
        }

        // Wait for each thread to finish
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i].join();
        }

        // Calculate time passed
        clock = System.currentTimeMillis() - clock;

        // Store top ten primes
        int[] topTen = new int[10];

        // Go big to small to get top ten, skip evens
        for (int i = MAX - 2; i >= 3; i -= 2) {
            if (isPrime[i]) {
                totalSum += i;
                totalNum++;
                if (totalNum <= 10)
                    topTen[10 - totalNum] = i;
            }
        }

        // Add in 2 at the end
        totalSum += 2;
        totalNum++;

        // Print output
        System.out.println(clock + " " + totalNum + " " + totalSum);
        for (int i = 0; i < 10; i++)
            System.out.print(topTen[i] + " ");
        System.out.println();
    }

    // Threads
    static class SieveThread implements Runnable {
        // Thread range
        private int lo, hi;
    
        // Constructor
        public SieveThread(int lo, int hi) {
            this.lo = lo;
            this.hi = Math.min(hi, MAX);
        }
    
        // Thread method
        public void run() {
            // For all numbers up to square root
            // Skip evens (those aren't prime... except 2)
            for (int i = 3; (long) i < SQRT_MAX; i += 2) {
    
                // Find starting point
                int temp = i * i;

                // Make sure starting point is in this range
                if (temp < lo)
                    temp = ((lo + i - 1) / i) * i;
                
                // Find other multiples in this range and set them to false
                for (int j = temp; j < hi; j += i) {
                    isPrime[j] = false;
                }
            }
        }
    }
}
