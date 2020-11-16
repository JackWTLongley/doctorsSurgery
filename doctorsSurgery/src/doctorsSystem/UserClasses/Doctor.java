package doctorsSystem.UserClasses;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import doctorsSystem.Encryption;
import doctorsSystem.Main;

public class Doctor
{
	//Creating and defining all variables
	private static String username;
   	private static String doctorName;
   	private static String doctorAddress;
   	private static String doctorPostcode;
   	private static String doctorDOB;
   	static int amountOfPatients = 0;
   	static int amountOfDrugsPerscribed = 0;
   	//Creating a resizeable arraylist for both patients and doctors
   	static ArrayList <Patient> patients; 
   	static ArrayList <DrugsPrescribed> dHistory;

    public Doctor(String inputUsername) throws FileNotFoundException
    {
    	this.username = inputUsername;
   		//Decrypting the file
		String encyrptedFile = ("data\\usersStorage\\"+username+".txt");
   	   	Encryption.decrypt(new File(encyrptedFile));
    	//Creating a Scanner instance to read the file
   		String fileName = ("data\\usersStorage\\"+username+".txt");
		Scanner read = new Scanner(new FileReader(fileName));
		
		//reading the whole file
		while(read.hasNext()) {
			String temp = read.next();
			if (temp.equals("0")) {
				this.doctorName = read.next();
				this.doctorAddress = read.next();
				this.doctorPostcode = read.next();
				this.doctorDOB = read.next();
			} else if (temp.equals("1")) {
				//Creates a new patient object for each patient who this doctor is their first choice 
				Patient patientTemp = new Patient(read.next());
				//Adding to the array list
				patients.add(patientTemp);
				amountOfPatients++;
			}else if (temp.equals("2")) {
				//Adds a new drug object for each drug prescribed
				String drug = read.next();
				String date = read.next();
				String patient = read.next();
				DrugsPrescribed History = new DrugsPrescribed(drug, date, inputUsername, patient);
				//Adding to the array list
				dHistory.add(History);
				amountOfDrugsPerscribed++;
			}
			
		}
		//Making sure the file is then encrypted to avoid time spent in plain text
		Encryption.encrypt(new File(encyrptedFile));
    }
    
    public static void openDoctor(String inputUsername) throws IOException {
   		try {
   			//Creating a new doctor object
   			new Doctor(inputUsername);
   			int i = 0;
   			while(i != 1){
   				String answer=null;
   				answer = display();
   				if(answer.equals("1")) {
   					addPerscription();
   				}else if(answer.equals("2")) {
   					showPatients();
   				}else if(answer.equals("3")) {
   					listPatientsVisits();
   				}else if(answer.equals("4")) {
   					listPerscriptions();
   				}else if(answer.equals("Q")) {
   					saveToFile();
   					i++;
   				}
   			}
		} catch (FileNotFoundException e) {
			
		}
   	}

	private static String display() {
		
		System.out.println("1 - Prescribe drugs to a patient");
		System.out.println("2 - List his/her patients");
		System.out.println("3 - List each patient’s visits to the surgery");
		System.out.println("4 - List drugs prescribed by him/her");
		System.out.println("Q - Logout");
		System.out.println("Pick an option");
		String answer = Main.console.next();
		return answer;
	}
	
	private static void addPerscription() {
		
		System.out.println("Prescription Menu");
		//displaying each user
		for(int i = 0; i != amountOfPatients; i++) {
			Patient displayPatient = patients.get(i);
			System.out.println(displayPatient.getUserName());
		}
		System.out.println("Enter user to perscribe to : ");
		String userToPerscribe = Main.console.next();
		System.out.println("Enter drug to perscribe : ");
		String drug = Main.console.next();
		//Adding todays date from the system time
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
		Date date = new Date();
		//Adding this new prescription to the patient
		int indexTempPatient = patients.indexOf(userToPerscribe);
		Patient tempPatient = patients.get(indexTempPatient);
		//Adding the new prescription to this patient
		tempPatient.addPerscription(drug, dateFormat.format(date), username);
		//Saving this patient update to the file system
		try {
			tempPatient.saveToFile();
		} catch (IOException e) {
			System.out.println("IOException");
		}

	}
	
	private static void showPatients() {
		int i = 0;
		while (i != amountOfPatients) {
			Patient tempPatient = patients.get(i);
			System.out.println(tempPatient.getUserName());
		}
	}
	
	private static void listPatientsVisits() {
		int i = 0;
		while (i != amountOfPatients) {
			Patient tempPatient = patients.get(i);
			tempPatient.getVisit();
		}
	}
	
	public static void listPerscriptions() {
		System.out.println("Prescription Menu");
		int i = 0;
		while (i != amountOfDrugsPerscribed) {
			DrugsPrescribed tempDrug = dHistory.get(i);
			tempDrug.displayDrugs();
			i++;
		}
	}
	
	public static void addPatient(String username2) throws FileNotFoundException {
		Patient patientTemp = new Patient(username2);
		//Adding to the array list
		patients.add(patientTemp);
		amountOfPatients++;
	}
	
	public static void saveToFile() throws IOException {
		String fileName = "data/usersStorage/"+username+".txt"; 
		//BufferedWriter a StringJoiner instance with append mode off
		BufferedWriter bufferWritter = new BufferedWriter(new FileWriter(fileName));
		//Creating a StringJoiner instance with spaces between the joins
		StringJoiner sj = new StringJoiner(" ");
		//Adding all the personal data
		sj.add("0");
		sj.add(doctorName);
		sj.add(doctorAddress);
		sj.add(doctorPostcode);
		sj.add(doctorDOB);
		bufferWritter.append(sj.toString());
		//Creating a new line
		bufferWritter.newLine();
		//Clearing the StringJoiner
		sj = new StringJoiner(" ");
		int i = 0;
		while(i != amountOfPatients) {
			//Adding 1 to define it is part of the patients list
			sj.add("1");
			Patient tempPatient = patients.get(i);
			sj.add(tempPatient.getUserName());
			tempPatient.saveToFile();
			bufferWritter.append(sj.toString());
			bufferWritter.newLine();
			//Clearing the StringJoiner
			sj = new StringJoiner(" ");
			i++;
		}
		i = 0;
		while(i != amountOfDrugsPerscribed) {
			//Adding 2 to define it is part of the drugs prescribed list
			sj.add("2");
			DrugsPrescribed tempDrug = dHistory.get(i);
			sj.add(tempDrug.getPrescriptionDoctor());
			bufferWritter.append(sj.toString());
			bufferWritter.newLine();
			//Clearing the StringJoiner
			sj = new StringJoiner(" ");
			i++;
		}
		bufferWritter.close();
		//Making sure the data is encrypted
		Encryption.encrypt(new File(fileName));
	}
}