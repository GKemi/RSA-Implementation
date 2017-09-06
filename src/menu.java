import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * Created by Gil1 on 13/11/2016.
 */
public class menu {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        Scanner userInput = new Scanner(System.in);
        int inputValue;
        System.out.println("Hello! Welcome to my RSA simulation program. Please choose from the options below.");

        //Makes the program run and allows the user to run specific simulations as many times as they want
        while (true){
            System.out.println("1: Basic RSA transaction");
            System.out.println("2: RSA failure (Charlie decrypts ciphertext)");
            System.out.println("0: End program");
            inputValue = userInput.nextInt();

            //If the input is 1 it runs the first simulation
            if (inputValue == 1){
                Simulations.simulationOne();
            } else if (inputValue == 2) { //If the input is 2, it runs the second simulation
                Simulations.simulationTwo();
            } else if (inputValue == 0){ //If the input is 0, it quits the program
                System.exit(0);
            } else { //Otherwise the user will be told to input one of the given options
                System.out.println(inputValue + " Is not an option. Please select one of the available options");
            }

        }
    }

}
