import java.math.BigInteger;
import java.util.ArrayList;
import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

/**
 * Created by Gil1 on 18/11/2016.
 */
public class NaivePrime {

    //Method using the sieve of erastothenes to get a list of primes to check by the composite key for finding the prime factors
    public static BigInteger[] bruteForceFactors(BigInteger primeComposite){
        ArrayList<BigInteger> primeList = new ArrayList<>();
        BigInteger[] primeSet = new BigInteger[2];
        boolean isPrime;

        //Prime checker that uses the sieve of eratosthenes to find prime numbers up to square root of prime composite
        for (BigInteger i = BigInteger.valueOf(2); i.multiply(i).compareTo(primeComposite) <= 0; i = i.add(ONE)) {
            isPrime = true;

            for (BigInteger j = BigInteger.valueOf(2); j.compareTo(i) <= 0; j = j.add(ONE)) {

                if (i.equals(j)) {
                    break;
                } else if (i.mod(j).equals(ZERO)) {
                    isPrime = false;
                    break;
                }

            }
            if (isPrime) {
                primeList.add(i);
            }
        }

        for (BigInteger element: primeList){
            if (primeComposite.mod(element).equals(ZERO)){
                primeSet[0] = element;
                primeSet[1] = primeComposite.divide(primeSet[0]);
                break;
            }
        }

        return primeSet;

    }

    //Naive method for checking primes
    public static boolean isPrime(BigInteger primeCheck){

        if (primeCheck.compareTo(BigInteger.valueOf(2)) < 0 || primeCheck.mod(BigInteger.valueOf(2)).equals(ZERO)){
            return false;
        }

        boolean primeNumber = true;

        for (BigInteger i = BigInteger.valueOf(2); i.compareTo(primeCheck) < 0; i = i.add(ONE)) {

            if (primeCheck.equals(i)) {
                break;
            } else if (primeCheck.mod(i).equals(ZERO)) {
                primeNumber = false;
                break;
            }

        }

        return primeNumber;
    }

}
