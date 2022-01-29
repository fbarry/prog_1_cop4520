# prog_1_cop4520

Compile and run with `javac MainThread.java` and `java MainThread`

I used the Sieve of Eratosthenes which has a sequential runtime of O(n log(log(n))). My algorithm separates this into 8 sections operating in parallel, dividing runtime by 8. Additionally, I skip all even numbers, a non-trivial speedup of 2. 

The "primes.txt" file shows my proof of correctness.
