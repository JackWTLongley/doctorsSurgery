package doctorsSystem.UserClasses;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringJoiner;

import doctorsSystem.CreateUser;
import doctorsSystem.Encryption;
import doctorsSystem.Main;


public class SysAdmin {
	
	static ArrayList <String> patients = new ArrayList<String>();
	static ArrayList <String> doctors = new ArrayList<String>();
	static int amountOfPatients = 0; 
	static int amountOfDoctors = 0;
	
	private static void mainProcess() throws IOException {
		//Decrypting the file
		String encyrptedFile = ("data\\sysAdmin\\doctors.txt");
   	   	Encryption.decrypt(new File(encyrptedFile));
   	   	encyrptedFile = ("data\\sysAdmin\\patients.txt");
	   	Encryption.decrypt(new File(encyrptedFile));
   		
	   	//Reading the file to populate the array lists
	   	String fileName = ("data\\sysAdmin\\doctor.txt");
		Scanner read = new Scanner(new FileReader(fileName));
		while(read.hasNext()) {
			String tempDoctor = read.next();
			doctors.add(tempDoctor);
			amountOfDoctors++;
		}
		read.close();
	   	//Reading the file to populate the array lists
		fileName = ("data\\sysAdmin\\patients.txt");
		read = new Scanner(new FileReader(fileName));
		while(read.hasNext()) {
			String tempPatient = read.next();
			patients.add(tempPatient);
			amountOfPatients++;
		}
		read.close();
		//Encrypting the file
		String decryptedFile = ("data\\sysAdmin\\doctors.txt");
   	   	Encryption.encrypt(new File(decryptedFile));
   	   	decryptedFile = ("data\\sysAdmin\\patients.txt");
	   	Encryption.encrypt(new File(decryptedFile));
	   	int i = 0;
	   	while(i != 1) {
			String answer = null;
			answer = display();
			if (answer.equals("1")) {
				listDoctor();
			} else if (answer.equals("2")) {
				listPatients();
			} else if (answer.equals("3")) {
				listDrugs();
			} else if (answer.equals("4")) {
				listVisits();
			} else if (answer.equals("5")) {
				System.out.println("1 - Add");
				System.out.println("2 - Remove");
				answer = Main.console.next();
				if (answer.equals("1")) {
					CreateUser.createUserMain();
					patients.clear();
					doctors.clear();
					mainProcess();
				}else if (answer.equals("2")) {
					removeUser();
				}
			} else if (answer.equals("Q")) {
				saveToFile();
				i++;
			} else {
				System.out.println("Please enter a valid option!");
			}
	   	}
}
	
	public static void openSysAdmin() {
		try {
			mainProcess();
		} catch (IOException e) {
			System.out.println("IOException");
		}
	}

	private static String display() {
		
		System.out.println("1 - List doctors");
		System.out.println("2 - List patients");
		System.out.println("3 - List drugs prescribed per doctor (or per patient), if access is allowed");
		System.out.println("4 - List previous/future patient visits (including date and ailments).");
		System.out.println("5 - Add/edit/remove doctors/patients");
		System.out.println("Q - Logout");
		System.out.println("Pick an option");
		String answer = Main.console.next();
		return answer;
	}
	
	private static void listDoctor() throws IOException {
		int i = 0;
		System.out.println("Doctor List");
		while (i != amountOfDoctors) {
			System.out.println(doctors.get(i));
		}
	}
	
	private static void listPatients() throws IOException {
		int i = 0;
		System.out.println("Patient List");
		while (i != amountOfPatients) {
			System.out.println(patients.get(i));
		}
	}
	
	private static void listDrugs() throws IOException {
		System.out.println("1 - Doctor");
		System.out.println("2 - Patient");
		String user = Main.console.next();
		
		System.out.println("Enter username to access drugs perscribed : ");
		String username = Main.console.next();
		
		if(user.equals("1")) {
			Doctor doctorToView = new Doctor(username);
			doctorToView.listPerscriptions();
		} else if(user.equals("2")) {
			Patient patientToView = new Patient(username);
			if(patientToView.getPrivacy()) {
				patientToView.listPerscriptions();
			} else {
				System.out.println("Access Denied");
			}
		} else {
			System.out.println("Incorrect Data Input");
		}
	}
	
	public static void listVisits() throws FileNotFoundException {
		System.out.println("Enter username to list visits : ");
		String username = Main.console.next();
		
		Patient patientToView = new Patient(username);
		System.out.println(username + "'s visits");
		patientToView.getVisit();
	}
	
	public static void addDoctor(String username) throws IOException {
		
		String fileName = "data\\sysAdmin\\doctors.txt"; 
		BufferedWriter bufferWritter = new BufferedWriter(new FileWriter(fileName, true));
		
		try {
			bufferWritter.newLine();
			bufferWritter.append(username);
		} catch(Exception e) {
			
		} finally {
			bufferWritter.close();
		}
	}
	
	public static void addPatient(String username) throws IOException {
		String fileName = "data\\sysAdmin\\patients.txt"; 
		BufferedWriter bufferWritter = new BufferedWriter(new FileWriter(fileName, true));
		
		try {
			bufferWritter.newLine();
			bufferWritter.append(username);
		} catch(Exception e) {
			
		} finally {
			bufferWritter.close();
		}
	}
	
	public static void removeUser() throws IOException {
		System.out.println("Enter username to delete : ");
		String username = Main.console.next();
		File File = new File("data\\usersStorage\\"+username+".txt");
		if(File.delete()) {
			System.out.println("Success");	
			//Clearing and re-populating the array
			patients.clear();
			doctors.clear();
			mainProcess();
		} else {
			System.out.println("Failed");
		}
	}
	
	private static void saveToFile() throws IOException {
		//Saving all the data
		int i = 0;
		while(i != amountOfPatients) {
			String patientName = patients.get(i);
			Patient tempPatient = new Patient(patientName);
			tempPatient.saveToFile();
		}
		i = 0;
		while(i != amountOfDoctors) {
			String doctorName = doctors.get(i);
			Doctor tempDoctor = new Doctor(doctorName);
			tempDoctor.saveToFile();
		}
		
	}
	
}
