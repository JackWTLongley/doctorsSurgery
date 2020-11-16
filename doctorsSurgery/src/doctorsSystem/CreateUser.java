package doctorsSystem;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.StringJoiner;

import doctorsSystem.UserClasses.SysAdmin;

public class CreateUser {
	
	static Scanner console = new Scanner(System.in);
	static String username = "";
	static String hashedPassword = "";
	static String credentials = "";
	static String creationError = "";
	
	public static void createUserMain() {
		int i = 0;
		String creationError = "";
		while(i != 1) {
			
			System.out.println(creationError);
			username = makeUserName();
			if(username.equals("error")) {
				creationError = "Error 201 : Bad Username";
			} else {
				hashedPassword = makePassword();		
				if(hashedPassword.equals("error")) {
					creationError = "Error 202 : Bad Password";
				} else {
					credentials = makeCredentials();
					if(credentials.equals("0")||credentials.equals("1")||credentials.equals("2")) {
						try {
							System.out.println("Your username is : ");
							System.out.println(username);
							saveToFile();
							CreateProfile.createProfileMain(username, credentials);
							
							if(credentials.equals("0")) {
								SysAdmin.addPatient(username);
							} else if(credentials.equals("1")) {
								SysAdmin.addDoctor(username);
							}
							
						} catch (IOException ex) {
							creationError = "Error 204 : No File Found (Saving)";
				        }
						i++;
					}
					else {
						creationError = "Error 203 : Bad Credentials";
					}
				}
			}
		}
	}

	private static String makeUserName() {
		
		System.out.println("Username requirements;");
		System.out.println("Real name.");
		System.out.println("All Capitals");
		System.out.println("If name already exists,");
		System.out.println("the system will increment it.");
		String inputUsername = console.next();
		
		inputUsername = inputUsername.toUpperCase();
		//Searching the users.txt for users with the same name
		String exists = usernameSearch(inputUsername);
		StringBuilder sb = new StringBuilder(inputUsername);
		//Adds the number to the user name to stop conflictions
		sb.append(exists);
		return sb.toString();
		
	}

	private static String usernameSearch(String inputUsername) {
		String fileName = ("data\\users.txt");
		FileReader file = null;
		Scanner read = null;
		int finalNumber = 0;
		try {
			file = new FileReader(fileName);
			read = new Scanner(file);
			//reading the whole file
			while(read.hasNext()) {
				String returnedValue = read.next();
				//Performing a linear search and incrementing the integer that meet the user name
				//Passing into a separate method as try-catch should not be nested
				finalNumber = usernameExists(returnedValue, inputUsername, finalNumber);
				read.nextLine();
			}
		} catch (Exception ex) {
			creationError = "Error 204 : File not found (Search)";
		}
		read.close();
		//This is to check the number isn't 0 as if it was zero
		//It would append a zero to the user name
		if(finalNumber != 0) {
			return Integer.toString(finalNumber);
		}else {
			return ("");
		}
	}

	private static int usernameExists(String returnedValue, String inputUsername,int finalNumber) {
		try {
			//Checking if the substring matches the input name
			//Using substring as to remove the numbers stored at the end of a user name
			//Incrementing the number if a match is found
			if((returnedValue.substring(0, inputUsername.length())).equals(inputUsername)) {
				finalNumber++;
			}
		//The catch is here as it will throw an exception if the name is not long enough
		} catch (Exception ex) {
			
		}
		return finalNumber;
	}
	
	private static String makePassword() {
		System.out.println("Password Requirements;");
		System.out.println("Over 12 characters long.");
		System.out.println("An Uppercase letter (A-Z).");
		System.out.println("A Lowercase letter (a-z).");
		System.out.println("A Digit (0-9).");
		System.out.println("A Special character (~`!@#$%^&*()+=_-{}[]\\|:;”’?/<>,.).");
		System.out.println("Enter Password : ");
		String inputPassword = console.next();
		//Checking that the password meets all the criteria above
		if (meetRequirements(inputPassword)) {
			//Hashing the password as it shouldn't be stored in plain text
			return Encryption.hashPassword(inputPassword);
		}
		return "error";
	}

	private static boolean meetRequirements(String password) {
		int upperCase = 0;
		int lowerCase = 0;
		int digit = 0;
        int special = 0;
        
		//Checking the length first for the password
        if(password.length() < 12) {
			return false;
		} 
		else {
			//For each letter it runs this check
			for(int i =0; i<password.length(); i++){
	            char character = password.charAt(i);
	            //Checking if upper case
	            if(Character.isUpperCase(character)){
					upperCase++;
	            }
	            //Checking if lower case
	            if(Character.isLowerCase(character)){
					lowerCase++;
	            }
	            //Checking if it is a digit
	            if(Character.isDigit(character)){
					digit++;
	            }
	            //checking if its ASCII code is one of a special character
	            if(character>=33 && character<=47||character>=58 && character<=64||
	               character>=91 && character<=96||character>=123 && character<=126){
					special++;
	            }
			}
		}
        //if it contains one of each return true
		if(special !=0 && lowerCase != 0 && upperCase !=0 && digit !=0) {
			return true;
		} 
		else {
			return false;
		}
	}

	private static String makeCredentials() {
		System.out.println("Please enter user credential score");
		System.out.println("0 = Paitent");
		System.out.println("1 = Doctor");
		System.out.println("2 = SysAdmin");
		System.out.println("Enter : ");
		return console.next();
	}

	private static void saveToFile () throws IOException {
		String fileName = "data/users.txt"; 
		//Creating the BufferedWriter instance with append mode on
		BufferedWriter bufferWritter = new BufferedWriter(new FileWriter(fileName, true));
		StringJoiner sj = new StringJoiner(" ");
		sj.add(username);
		sj.add(hashedPassword);
		sj.add(credentials);
		bufferWritter.newLine();
		//Writing all the data on to the end of the file
		bufferWritter.append(sj.toString());
		bufferWritter.close();
		
	}


}
