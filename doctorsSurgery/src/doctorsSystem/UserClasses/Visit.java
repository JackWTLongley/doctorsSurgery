package doctorsSystem.UserClasses;


public class Visit {
	static int visitID;
	static String patientName;
	static String visitDate;
	static String doctorName;
	static String description;
	
	public Visit(int visitID, String patientName, String date, String docName, String description) {
		Visit.visitID = visitID;
		Visit.patientName = patientName;
		Visit.visitDate = date;
		Visit.doctorName = docName;
		Visit.description = description;
	}
	
	public static void displayVisit() {
		System.out.println(visitID + " - " + patientName + " - " + visitDate + " - " + doctorName + " - " + description);
	}
   
	public static String getVisit() {
		return (visitID + " " + visitDate + " " + description + " " + doctorName);
	}
	
	public static int getVisitID() {
		return visitID;
	}
	
}