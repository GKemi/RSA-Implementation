import java.math.BigInteger;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

/**
 * Created by Gil1 on 14/11/2016.
 */
public class RabinMiller {

    //Implemented primality test using the rabin miller algorithm
    //Code was initially develoepd on my own, but then polished by reviewing the following implemenation:
    //https://en.wikibooks.org/wiki/Algorithm_Implementation/Mathematics/Primality_Testing
    public static boolean isPrime(BigInteger primeCheck){
        if (primeCheck.mod(BigInteger.valueOf(2)).equals(ZERO)){ //Checks if number is even
            return false;
        }

        boolean isPrime = false; //Initialises value as being false
        int exponentValue = 1; //Exponent used for finding the value of d
        BigInteger n = primeCheck; //Sets n to be the input prime number, to be checked
        BigInteger TWO = new BigInteger("2"); //Value of 2 for a BigInteger
        BigInteger nMinusOne = n.subtract(ONE); //Value of n - 1
        BigInteger dValue = nMinusOne; //The value of d starts off as n-1

        while (dValue.mod(TWO).equals(ZERO)) {
            exponentValue++; //With ever iteration the exponent value is incremented
            dValue = dValue.divide(TWO); //With every iteration the value of d is divided by 2, until d is no longer divisible by 2
        }

        //Starts a loop that begins checking, and thusly determining, if the number is a probable prime
        for (int i = 0; i < 5; i++){
            BigInteger aValue = RSA.generateRandomOfRange(TWO, nMinusOne.subtract(TWO)); //Random number within a given range
            BigInteger bValue = aValue.modPow(dValue, n); // a^d % n

            if (bValue.equals(ONE) || bValue.equals(nMinusOne)){ //If either of these conditions are true, it will go to the next iteration of the loop
                continue;
            }

            for (int j = 1; j < exponentValue; j++){ //Nested loop that starts  if the condition of above statement is not true
                bValue = bValue.modPow(TWO, n); //b^2 % n

                if (bValue.equals(ONE)){ //If the new value of b = 1, then the input number is not prime
                    isPrime = false;
                    break;
                } else if (bValue.equals(nMinusOne)){ //If the new value of b = n-1, then the input number is prime
                    isPrime = true;
                    break;
                }
            }
        }

        return isPrime; //Returns the variable at the end of the method, with the stored value
    }

}
