package doctorsSystem;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import doctorsSystem.UserClasses.Patient;
import doctorsSystem.UserClasses.Doctor;
import doctorsSystem.UserClasses.SysAdmin;

import java.io.File;

//**********************
//* Program Written By *
//*   Jack Longley     *
//**********************

public class Main 
{
	
	static int credentials = 0;
	static String userMessage = "";
	public static Scanner console = new Scanner(System.in);
	
	public static void main(String[] args)
	{	
		int i = 0;		
		while(i != 1) {
			String answer = null;
			answer = displayMainMenu();
			if (answer.equals("1")) {
				userLogin();
			} else if (answer.equals("Q") || answer.equals("q")) {
				i++;
			}
			else {
				userMessage = ("Error 100 : Please enter a valid option!");
			}
		}
		//Making sure console is closed before the program terminates
		console.close();
	}

	public static String displayMainMenu()
	{
		System.out.println("");
		System.out.println("- - Doctor/Patient System - -");
		System.out.println(userMessage);
		System.out.println("     - - MAIN MENU - -");
		System.out.println("1 - Login");
		System.out.println("Q - Quit");
		System.out.println("Pick an option");
		userMessage = "";
		String answer = console.next();
		console.nextLine();
		return answer;
	}
	
	private static String userLogin(){
		System.out.println("Enter Username : ");
		String inputUsername = console.next();
		System.out.println("Enter Password : ");
		String inputPassword = console.next();
		
		try {
			if(checkInput(inputUsername, inputPassword)) {
				if (credentials == 0){
					Patient.openPaitent(inputUsername);
				}
				if (credentials == 1){
					Doctor.openDoctor(inputUsername);
				}
				if (credentials == 2){
					SysAdmin.openSysAdmin();
				}
			} else {
				userMessage = "Error 101 : No such account exists";
			}
		} catch (Exception e) {
			userMessage = ("Error 102 : File not found (Login)");
		}
		return "";
	}
	
	public static boolean checkInput(String inputUsername,String inputPassword) throws FileNotFoundException {
		String fileName = ("data\\users.txt");
		FileReader file = new FileReader(fileName);
		Scanner read = new Scanner(file);
		//Reading the whole users.txt
		//if both criteria match return true
		//else return false
		while(read.hasNext()) 
		{
			if(read.next().equals(inputUsername)) {
				//Checking the password hashes match
				if(read.next().equals(Encryption.hashPassword(inputPassword))) {
					credentials = read.nextInt();
					read.close();
					return true;
				}
			} else {
				read.nextLine();
			}
		}
		read.close();
		return false;
	}
	
	public static void encrypt(String userArea){
        
		//Forming the file variable
		File file = new File("data\\usersStorage\\"+ userArea +".txt");
         
        try {
        	Encryption.encrypt(file);
        } catch (Exception ex) {
        	userMessage = "Error 105 : File not found (Encrypt)";
        }
	}
	
	public static void decrypt(String userArea) {
        
		//Forming the file variable
		File file = new File("data\\usersStorage\\"+ userArea +".txt");
        
        try {
        	Encryption.decrypt(file);
        } catch (Exception ex) {
        	userMessage = "Error 106 : File not found (Decrypt)";
        }
	}
}
