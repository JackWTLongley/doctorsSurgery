package doctorsSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
 
public class Encryption {
	private static String key = "VmYq3t6w9z$C&F)J";
    
    public static String hashPassword(String inputPassword){
    	//Protecting the Algorithm
    	return doHashingAlgorithm(inputPassword);
    }
    
    private static String doHashingAlgorithm(String inputPassword) {
		String hashedPassword = null;
        try {
            //Create a MessageDigest instance for SHA-512
            MessageDigest sha = MessageDigest.getInstance("SHA-512");
            //Add password bytes to digest
            sha.update(inputPassword.getBytes());
            //Get the hash's bytes 
            byte[] hashBytes = sha.digest();
            //bytes[] contains bytes in decimal format;
            //Convert it to hexadecimal
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< hashBytes.length ;i++)
            {
            	sb.append(Integer.toString((hashBytes[i] & 0xff) + 0x100, 16).substring(1));
            	//Integer.toString(src, 16) converts the integer to a hexadecimal
                //& 0xFF performs a binary AND, causing the returning value to be between 0 and 255(FF is 255 in hexadecimal)
            	//+ 0x100 adds 256 to the result to ensure the result is always 3 digits
            	//Finally .substring(1) strips the first character (the 1 from step 2)
            }
            //Get complete hashed password in hexadecimal format
            hashedPassword = sb.toString();
        } 
        catch (Exception ex) 
        {
        	System.out.println("Error 301 : Error Hashing");
        }
        return hashedPassword;
	}
    
    public static void encrypt(File file) {
        //passes in the mode, secret key and files to the "doEncyrption" subRoutine
    	doEncyrption(Cipher.ENCRYPT_MODE, file);
    }
 
    public static void decrypt(File file) {
    	//passes in the mode, secret key and files to the "doEncyrption" subRoutine
        doEncyrption(Cipher.DECRYPT_MODE, file);
    }
 
    private static void doEncyrption(int cipherMode, File file){
        try {
            //encodes 'key' into 'secretKey' changing the text of the string to bytes and uses AES Algorithm
        	Key secretKey = new SecretKeySpec(key.getBytes(), "AES");
            //sets the cipher to AES
        	Cipher cipher = Cipher.getInstance("AES");
            //initialises the cipher with the mode(either decrypt or encrypt) and the new secret key
        	cipher.init(cipherMode, secretKey);
             
        	//FileInputStream obtains the raw bytes from the file
            FileInputStream inputStream = new FileInputStream(file);
            byte[] inputBytes = new byte[(int) file.length()];
            //reads the raw bytes from 'inputBytes'
            inputStream.read(inputBytes);
           
            byte[] outputBytes = cipher.doFinal(inputBytes);
            
            //writes to the file
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(outputBytes);
             
            inputStream.close();
            outputStream.close();
             
        } 
        catch (Exception ex) {
        	System.out.println("Error 302 : Error encrypting/decrypting the file");
        }
    }
}
