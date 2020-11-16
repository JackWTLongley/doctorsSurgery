package doctorsSystem.UserClasses;

public class DrugsPrescribed {

	static String drug; 
	static String date;
	static String doctor;
	static String patient;
	
	public DrugsPrescribed(String drug, String date, String doctor, String patient) {
		DrugsPrescribed.drug = drug;
		DrugsPrescribed.date = date;
		DrugsPrescribed.doctor = doctor;
		DrugsPrescribed.patient = patient;
	}
	
	public static void displayDrugs() {
		System.out.println(drug + " " + date + " " + doctor + " " + patient);
	}

	public static String getPrescription() {
		return (drug + " " + date + " " + doctor);
	}
	
	public static String getPrescriptionDoctor() {
		return (drug + " " + date + " " + patient);
	}
}
