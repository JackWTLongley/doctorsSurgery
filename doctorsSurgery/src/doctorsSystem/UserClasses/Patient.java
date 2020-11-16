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

public class Patient
{
	private static String username;

	private static String patientName;
	private static String patientAddress;
	private static String patientPostCode;
	private static String patientDOB; 
	private static String FirstChoiceDoc; 
	//0 Means The surgery admin cannot view the file
	//1 Means The surgery admin can
	private static String privacySetting; 
	static int amountOfVisits = 0;
	static int amountOfDrugs = 0;
	//Creating a resizeable arraylist for both patients and doctors
	static ArrayList <Visit> vHistory = new ArrayList<Visit>();
	static ArrayList <DrugsPrescribed> dHistory = new ArrayList<DrugsPrescribed>();
   
   	public Patient(String inputUsername) throws FileNotFoundException {
   		this.username = inputUsername;
	   	
   		String FileName = ("data\\usersStorage\\"+inputUsername+".txt");
   	   	File Encryptedfile = new File(FileName);
   	   	//Decryting the file
   	   	Encryption.decrypt(Encryptedfile);
    	
   		FileReader fileReader = new FileReader(FileName);
		Scanner read = new Scanner(fileReader);
		
		//reading the whole file
		while(read.hasNext()) {
			String temp = read.next();
			if (temp.equals("0")) {
				//Populating the personal data
				this.patientName = read.next();
				this.patientAddress = read.next();
				this.patientPostCode = read.next();
				this.patientDOB = read.next();
				this.FirstChoiceDoc = read.next();
				this.privacySetting = read.next();
				
			}else if (temp.equals("1")) {
				//Creating a visit object
				int visitID = read.nextInt();
				String date = read.next();
				String docName = read.next();
				String description = read.next();
				Visit History = new Visit(visitID,username, date, description, docName);
				vHistory.add(History);
				amountOfVisits++;
				read.nextLine();
			}else if (temp.equals("2")) {
				//Creating a drugsprescribed object
				String drug = read.next();
				String date = read.next();
				String doctor = read.next();
				DrugsPrescribed History = new DrugsPrescribed(drug, date, doctor, inputUsername);
				dHistory.add(History);
				amountOfDrugs++;
			}
		}
		//Making sure the file is then encrypted to avoid time spent in plain text
		Encryption.encrypt(Encryptedfile);
		try {
			fileReader.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
   
   	public static void openPaitent(String inputUsername) {
   		try {
   			new Patient(inputUsername);
   			int i = 0;
   			while(i != 1){
   				String answer=null;
   				answer = display();
   				if(answer.equals("1")) {
   					bookVisit();
   				}else if(answer.equals("2")) {
   					try {
						changeFirstChoiceDoc();
					} catch (IOException e) {
						System.out.println("IOException");
					}
   				}else if(answer.equals("3")) {
   					listPerscriptions();
   				}else if(answer.equals("4")) {
   					getVisit();
   				}else if(answer.equals("5")) {
   					setPrivacy();
   				}else if(answer.equals("Q")) {
   					i++;
   				}
   			}
		} catch (FileNotFoundException e) {
			
		}
   	}
   	
	public static String display() {

		System.out.println("1 - Book a visit to the surgery");
		System.out.println("2 - Add/change first choice doctor");
		System.out.println("3 - List previously prescribed drugs");
		System.out.println("4 - List previous/future visits to the surgery");
		System.out.println("5 - Privacy settings");
		System.out.println("Q - Logout");
		System.out.println("Pick an option");
		String answer = Main.console.next();
		return answer;
	}
	
	public static void bookVisit() {
		System.out.println("Enter Reason : ");
		String description = Main.console.next();
		System.out.println("Enter date (dd/mm/yy) : ");
		String date = Main.console.next();
		//Creating a random number for the visit ID
		int visitID = (int) (Math.random() * (10000 - 1 + 1) + 1);
		int i = 0;
		//Checking if the visit id is already used
		while(i != amountOfVisits) {
			Visit tempVisit = vHistory.get(i);
			int tempVisitID = tempVisit.getVisitID();
			//if visitid is already used generate a new one
			if(tempVisitID == visitID) {
				visitID = (int) (Math.random() * (10000 - 1 + 1) + 1);
			}
		}
		//Creating a new visit object
		Visit History = new Visit(visitID,username, date, description, FirstChoiceDoc);
		vHistory.add(History);
		amountOfVisits++;
	}
	
	public static void changeFirstChoiceDoc() throws IOException {
		System.out.println("Enter Doctor Name (All caps no spaces) : ");
		Patient.FirstChoiceDoc = Main.console.next();
		Doctor tempDoctor = new Doctor(FirstChoiceDoc);
		tempDoctor.addPatient(username);
		tempDoctor.saveToFile();
	}
	
	
	public String getUserName() {
		
		return username;
		
	}
	
	public void addPerscription(String drug, String date, String doctor) {
		DrugsPrescribed History = new DrugsPrescribed(drug, date, doctor, username);
		dHistory.add(History);

	}

	public static void listPerscriptions() {
		System.out.println("Prescription Menu");
		int i = 0;
		while (i != amountOfDrugs) {
			DrugsPrescribed tempDrug = dHistory.get(i);
			tempDrug.displayDrugs();
			i++;
		}
		
	}
	
	public static void getVisit() {
		int i = 0;
		while (i != amountOfDrugs) {
			Visit tempVisit = vHistory.get(i);
			tempVisit.displayVisit();
			i++;
		}
	}

	public static void setPrivacy() {
		System.out.println("0 - No Access");
		System.out.println("1 - Access");
		System.out.println("Enter an option : ");
		Patient.privacySetting = Main.console.next();
	}
	
	public boolean getPrivacy() {
		if(privacySetting.equals("0")) {
			return false;
		} else {
			return true;
		}
	}
	
	public static void saveToFile() throws IOException {
		String fileName = "data/usersStorage/"+username+".txt"; 
		//BufferedWriter a StringJoiner instance with append mode off
		BufferedWriter bufferWritter = new BufferedWriter(new FileWriter(fileName));
		//Creating a StringJoiner instance with spaces between the joins
		StringJoiner sj = new StringJoiner(" ");
		//Adding all the personal data
		sj.add("0");
		sj.add(patientName);
		sj.add(patientAddress);
		sj.add(patientPostCode);
		sj.add(patientDOB);
		sj.add(FirstChoiceDoc);
		sj.add(privacySetting);
		bufferWritter.append(sj.toString());
		//Creating a new line
		bufferWritter.newLine();
		//Clearing the StringJoiner
		sj = new StringJoiner(" ");
		int i = 0;
		while(i != amountOfVisits) {
			//Adding 1 to define it is part of the visit list
			sj.add("1");
			Visit tempVisit = vHistory.get(i);
			sj.add(tempVisit.getVisit());
			bufferWritter.append(sj.toString());
			bufferWritter.newLine();
			//Clearing the StringJoiner
			sj = new StringJoiner(" ");
			i++;
		}
		i = 0;
		while(i != amountOfDrugs) {
			//Adding 2 to define it is part of the drugs list
			sj.add("2");
			DrugsPrescribed tempDrug = dHistory.get(i);
			sj.add(tempDrug.getPrescription());
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
