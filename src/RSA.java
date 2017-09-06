/**
 * Created by Gil1 on 07/11/2016.
 */

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

public class RSA {

    public  BigInteger phi, privateKey; //Variables needed for the keys
    private String message, ciphertext;
    private BigInteger [] publicKey, primeSet; //Used for the public key pair
    int maxChars; //Stores the maximum number of characters allowed for the input string
    ArrayList<Integer> characterLengths = new ArrayList<>(); //Arraylist for storing set of character lengths


    //Constructor initializes all of the variables required for the RSA class
    //Also generates primes, public and private keys
    public RSA(int bitLength) {
        maxChars = bitLength/4; //Stores the current bitLength value
        primeSet = new BigInteger[2]; //Pair of primes that represents p and q
        primeSet[0] = generatePrime(bitLength);
        primeSet[1] = generatePrime(bitLength);
        phi = primeSet[0].subtract(ONE).multiply(primeSet[1].subtract(ONE));
        publicKey = new BigInteger[2]; //Pair of values that represent the public key
        publicKey[0] = generatePublicKey(phi);
        publicKey[1] = primeSet[0].multiply(primeSet[1]);
        privateKey = generatePrivateKey(getPublicKey()[0], phi);
    }

    //Constructor for generating empty values
    public RSA(){
        primeSet = new BigInteger[2];
        primeSet[0] = ZERO;
        primeSet[1] = ZERO;
        phi = ZERO;
        publicKey = new BigInteger[2];
        publicKey[0] = ZERO;
        publicKey[1] = ZERO;
        privateKey = ZERO;
    }

    //Encrypts the input string
    public String encrypt(String message) {
        if (message.length() > maxChars){
            return messageChunksEncrypt(message);
        } else {
            BigInteger inputData = new BigInteger(message.getBytes());
            inputData = inputData.modPow(getPublicKey()[0], getPublicKey()[1]);
            return inputData.toString();
        }
    }

    //For strings that go over the bit length limit, this method encrypts the message appropriately by splitting the input into chunks
    public String messageChunksEncrypt(String message){
        ArrayList<String> messageChunks = new ArrayList<>();
        String chunk = "";
        for (int i = 0; i < message.length(); i++){
            chunk = chunk + message.substring(i, i+1);
            messageChunks.add(chunk);
            chunk = "";
        }

        BigInteger [] messageChunkBytes = new BigInteger[messageChunks.size()];
        String finalizedString = "", tempString;
        for (int i = 0; i < messageChunks.size();i++){
            BigInteger temp = new BigInteger(messageChunks.get(i).getBytes());
            messageChunkBytes[i] = encrypt(temp);
            tempString = messageChunkBytes[i].toString();
            finalizedString = finalizedString + tempString;
            characterLengths.add(tempString.length());
        }
        return finalizedString;
    }

    //Encrypts the inputted value
    public BigInteger encrypt (BigInteger message){
        return message.modPow(getPublicKey()[0], getPublicKey()[1]);
    }

    //Decrypts the input string
    public String decrypt(String message) {

        //Calls the message chunk decryption if the value of the character length variable has been previously modified
        if (!characterLengths.isEmpty()){
            return messageChunksDecrypt(message);
        } else {
            BigInteger inputData = new BigInteger(message);
            inputData = inputData.modPow(privateKey, getPublicKey()[1]);
            String decryptedMessage = new String(inputData.toByteArray());
            return decryptedMessage;
        }
    }

    //For strings that go over the bit length limit, this decrypts them appropriately by splitting the input into chunks
    public String messageChunksDecrypt(String message){
        ArrayList<BigInteger> messageChunks = new ArrayList<>();
        String chunk = "";

        for (int i = 0; i < characterLengths.size(); i++){
            chunk = chunk + message.substring(0, characterLengths.get(i));
            message = message.substring(characterLengths.get(i));
            messageChunks.add(new BigInteger(chunk));
            chunk = "";
        }

        String temp, finalizedString = "";
        BigInteger tempBigInt = ZERO;
        for (int i = 0; i < messageChunks.size(); i++){
            tempBigInt = decrypt(messageChunks.get(i));
            temp = new String(tempBigInt.toByteArray());
            finalizedString = finalizedString + temp;
        }

        return finalizedString;
    }

    //Decrypts the inputted value
    public BigInteger decrypt(BigInteger message){
        return message.modPow(privateKey, getPublicKey()[1]);
    }

    //Generates a public key e
    private static BigInteger generatePublicKey(BigInteger phi){
        BigInteger publicKey = generateRandomOfRange(ONE, phi.subtract(ONE));

        //Makes sure the value e and phi only have a GCD value of 1
        while (!publicKey.gcd(phi).equals(ONE)){
            publicKey = generateRandomOfRange(ONE, phi.subtract(ONE));
        }

        return publicKey;
    }

    //Uses extended euclidean algorithm to generate a private key
    public static BigInteger generatePrivateKey(BigInteger eKey, BigInteger phi) {

        BigInteger euclidArr[][] = new BigInteger[2][2];

        //Column one
        euclidArr[0][0] = phi;
        euclidArr[0][1] = eKey;

        //Column two
        euclidArr[1][0] = phi;
        euclidArr[1][1] = ONE;

        while (!euclidArr[0][1].equals(ONE)) {
            BigInteger multiplier = euclidArr[0][0].divide(euclidArr[0][1]);
            BigInteger colOneAns = euclidArr[0][0].subtract(euclidArr[0][1].multiply(multiplier));
            BigInteger colTwoAns = euclidArr[1][0].subtract(euclidArr[1][1].multiply(multiplier));

            if (colOneAns.compareTo(ZERO) == -1) {
                colOneAns = colOneAns.mod(phi);
            } else if (colTwoAns.compareTo(ZERO) == -1) {
                colTwoAns = colTwoAns.mod(phi);
            }

            euclidArr[0][0] = euclidArr[0][1];
            euclidArr[1][0] = euclidArr[1][1];

            euclidArr[0][1] = colOneAns;
            euclidArr[1][1] = colTwoAns;
        }

        return euclidArr[1][1];

    }

    //Generates a random number of a given bit length
    public static BigInteger generateRandomOfLength(int bitLength){
        BigInteger generatedNum;

        generatedNum = new BigInteger(bitLength, new Random());

        return generatedNum;
    }

    //Setters and Getters for specific variables
    public BigInteger[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(BigInteger[] publicKey) {
        this.publicKey = publicKey;
    }

    public String getCiphertext() {
        return ciphertext;
    }

    public ArrayList<Integer> getCharacterLengths() {
        return characterLengths;
    }

    public void setCharacterLengths(ArrayList<Integer> characterLengths) {
        this.characterLengths = characterLengths;
    }

    public void setCiphertext(String ciphertext) {
        this.ciphertext = ciphertext;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BigInteger[] getPrimeSet() {
        return primeSet;
    }

    public void setPrimeSet(BigInteger[] primeSet) {
        this.primeSet = primeSet;
    }

    //Generates a random prime of a given bit-length
    private static BigInteger generatePrime(int bitLength){
        BigInteger primeNum = generateRandomOfLength(bitLength);

        if (bitLength == 16){ //If the bit length is 16, it will generate a definite prime using the naive algorithm
            while(!NaivePrime.isPrime(primeNum)){
                primeNum = generateRandomOfLength(16);
            }
        } else {
            while (!RabinMiller.isPrime(primeNum)){
                primeNum = generateRandomOfLength(bitLength);
            }
        }

        return primeNum;
    }

    //Generates a random between a given range
    //This method borrowed elements from a Stack Overflow: http://stackoverflow.com/a/14006026
    public static BigInteger generateRandomOfRange(BigInteger minimum, BigInteger maximum){
        BigInteger generatedNum;

        do {
            generatedNum = new BigInteger(maximum.bitLength(), new Random());
        } while (generatedNum.compareTo(minimum) < 0 || generatedNum.compareTo(maximum) > 0);

        return generatedNum;
    }


}
