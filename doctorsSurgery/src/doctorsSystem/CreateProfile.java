package doctorsSystem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringJoiner;

public class CreateProfile {

   	private static String name;
   	private static String address;
   	private static String postcode;
   	private static String DOB;
   	private static String Privacy = "";
	
	public static void createProfileMain(String username, String credentials) throws IOException {
		
		System.out.println("Enter the name (Using _) : ");
		name = Main.console.next();
		
		System.out.println("Enter address (Using _) : ");
		address = Main.console.next();
		
		System.out.println("Enter postcode (Using _) : ");
		postcode = Main.console.next();
		
		System.out.println("Enter DOB (dd/mm/yy) : ");
		DOB = Main.console.next();
		
		if(credentials.equals("0")) {
			System.out.println("Enter privacy settings 0=No Access 1 = Access : ");
			Privacy = Main.console.next();
		}
		
		saveToFile(username);
		
	}
	
	private static void saveToFile (String username) throws IOException {
		String fileName = "data\\usersStorage\\" + username + ".txt"; 
		//Creating a FileWriter instance to write to a .txt file
		FileWriter fileWriter = new FileWriter(fileName);
		//Creating a StringJoiner instance to easily join the submit data together
		StringJoiner sj = new StringJoiner(" ");
		sj.add("1");
		sj.add(name);
		sj.add(address);
		sj.add(postcode);
		sj.add(DOB);
		sj.add(Privacy);
		//Writing to the file
		fileWriter.write(sj.toString());
		//Making sure the writer is closed
		fileWriter.close();
		//Encrypting the data
		Encryption.encrypt(new File(fileName));
	}
}
