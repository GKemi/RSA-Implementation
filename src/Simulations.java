import java.util.Scanner;
import static java.math.BigInteger.ONE;

/**
 * Created by Gil1 on 17/11/2016.
 */
public class Simulations {

    //Method for the first simulation
    public static void simulationOne(){
        Scanner simulationInput = new Scanner(System.in);
        System.out.println("Input size of the primes (bit length - recommended 512):");
        int bitLength = simulationInput.nextInt();
        simulationInput.nextLine();
        System.out.println("Generating primes...");
        RSA alice = new RSA(bitLength);
        RSA bob = new RSA();

        System.out.println("\n1. Alice Generates keys \t|\tCharlie N/A");
        System.out.println("Alice's 'e' key: " + alice.getPublicKey()[0] + "\nAlice's 'n' key: " + alice.getPublicKey()[1]);

        System.out.println("\n2. Alice sends the public key to Bob \t|\tCharlie gets the public key");
        bob.setPublicKey(alice.getPublicKey()); //Alice sends public key to Bob
        System.out.println("You are Bob. Enter a message:");
        bob.setMessage(simulationInput.nextLine()); //User inputs message

        System.out.println("\n3. Plaintext inputted! Bob now encrypts the message \t|\t Charlie N/A");
        bob.setCiphertext(bob.encrypt(bob.getMessage())); //Bob encrypts message

        System.out.println("\n4. Encrypted! Bob now sends generated ciphertext to Alice \t|\t Charlie receives ciphertext");
        alice.setCiphertext(bob.getCiphertext()); //Bob sends ciphertext to Alice
        alice.setCharacterLengths(bob.getCharacterLengths()); //Variable needed for determining the individual character lengths, if string is to big for bit length

        System.out.println("\n5. Alice decrypts the ciphertext \t|\t Charlie N/A");
        alice.setMessage(alice.decrypt(alice.getCiphertext())); //Alice decrypts the ciphertext

        System.out.println("\n6. Alice successfully decrypted the message! The message reads:\n" + alice.getMessage()
                            +"\nCharlie was unable to decrypt the ciphertext. The only content he obtained was:\n"
                            + "Public key (e,n): (" + alice.getPublicKey()[0] + "," + alice.getPublicKey()[1] + ")\n"
                            + "Ciphertext: " + alice.getCiphertext());


    }

    //Method for the second simulation
    public static void simulationTwo(){
        RSA alice = new RSA(16);
        RSA charlie = new RSA();
        RSA bob = new RSA();

        System.out.println("1. Alice Generates keys \t|\t Charlie N/A");

        System.out.println("\n2.Alice sends the public key (" + alice.getPublicKey()[0] +"," + alice.getPublicKey()[1]
                            + ") to Bob \t|\t Charlie obtains the public key and tries to brute force prime factors");
        bob.setPublicKey(alice.getPublicKey());
        charlie.setPublicKey(alice.getPublicKey());
        System.out.println("Bob is entering a message...");
        bob.setMessage("Bob is a national hero!!");
        charlie.setPrimeSet(NaivePrime.bruteForceFactors(charlie.getPublicKey()[1]));
        System.out.println("Bob's message: " + bob.getMessage());

        System.out.println("\n3.Plaintext inputted! Bob now encrypts the message \t|\t Charlie manages to find prime factors from n and is generating his own keys");
        System.out.println("Charlie's prime factors are...\nPrime one: " + charlie.getPrimeSet()[0] + "\nPrime Two: " + charlie.getPrimeSet()[1]);
        bob.setCiphertext(bob.encrypt(bob.getMessage()));
        charlie.phi = charlie.getPrimeSet()[0].subtract(ONE).multiply(charlie.getPrimeSet()[1].subtract(ONE)); //charlie calculate's phi
        charlie.privateKey = charlie.generatePrivateKey(charlie.getPublicKey()[0], charlie.phi); //charlie calculates the private key

        System.out.println("\n4.Encrypted! Bob now sends the generated ciphertext to Alice \t|\t Charlie receives ciphertext and works on decrypting it");
        charlie.setCiphertext(bob.getCiphertext());
        charlie.setCharacterLengths(bob.getCharacterLengths());
        alice.setCiphertext(bob.getCiphertext());
        alice.setCharacterLengths(bob.getCharacterLengths());

        System.out.println("\n5.Alice decrypts the message \t|\t Charlie decrypts the ciphertext");
        charlie.setMessage(charlie.decrypt(charlie.getCiphertext()));
        alice.setMessage(alice.decrypt(alice.getCiphertext()));
        System.out.println("Alice successfully decrypted the message! However, as did Charlie. the Value of n was very small. His deciphered message reads: \n"
                            + charlie.getMessage() + "\n");


    }

}
