import java.util.ArrayList;

// Manages patients and beds
// Demonstrates: 2D arrays, ArrayList, nested loops, sorting arrays,
//               passing an array to a method and using the length field
public class HospitalManager {
    private ArrayList<Patient> patients;
    private String[][] beds; // 4 rows, 5 columns = 20 beds

    // Constructor
    public HospitalManager() {
        patients = new ArrayList<Patient>();
        beds = new String[4][5]; // All null means available
    }

    // ========= PATIENT MANAGEMENT ==========

    public void registerPatient(Patient patient) {
        // Check for duplicate ID
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getPatientID().equalsIgnoreCase(patient.getPatientID())) {
                throw new IllegalArgumentException("Patient ID already exists.");
            }
        }
        patients.add(patient);
    }

    public Patient findPatient(String patientID) {
        for (int i = 0; i < patients.size(); i++) 
            if (patients.get(i).getPatientID().equalsIgnoreCase(patientID)) {
                return patients.get(i);
            }
        
        return null;
    }

    public void updatePatient(String patientID, String firstName, String lastName,
                              int age, String gender, String medicalCondition) {
        Patient p = findPatient(patientID);
        if (p == null) {
            throw new IllegalArgumentException("Patient not found.");
        }
        p.setFirstName(firstName);
        p.setLastName(lastName);
        p.setAge(age);
        p.setGender(gender);
        p.setMedicalCondition(medicalCondition);
    }

    public void deletePatient(String patientID) {
        Patient p = findPatient(patientID);
        if (p == null) {
            throw new IllegalArgumentException("Patient not found.");
        }
        // If inpatient has a bed, release it first
        if (p instanceof Inpatient) {
            Inpatient ip = (Inpatient) p;
            if (ip.getBedNumber() != null) {
                releaseBed(ip.getBedNumber());
            }
        }
        patients.remove(p);
    }

    public void displayAllPatients() {
        if (patients.size() == 0) {
            System.out.println("No patients registered.");
            return;
        }
        for (int i = 0; i < patients.size(); i++) {
            patients.get(i).displayDetails();
            System.out.println("--------------------");
        }
    }

    // ========== BED MANAGEMENT ==========

    public String allocateBed(String patientID) {
        Patient p = findPatient(patientID);
        if (p == null) {
            throw new IllegalArgumentException("Patient not found.");
        }
        if (!(p instanceof Inpatient)) {
            throw new IllegalArgumentException("Only Inpatients can get a bed.");
        }

        Inpatient ip = (Inpatient) p;
        if (ip.getBedNumber() != null) {
            throw new IllegalStateException("Patient already has a bed.");
        }

        // Find frst available bed using nested loops
        for (int row = 0; row < beds.length; row++) {
            for (int col = 0; col < beds[row].length; col++) {
                if (beds[row][col] == null) {
                    String bedNum = "B" + String.format("%02d", (row * 5) + col + 1);
                    beds[row][col] = patientID;
                    ip.setWardNumber(1);
                    ip.setBedNumber(bedNum);
                    return bedNum;
                }
            }
        }
        throw new IllegalStateException("No beds available.");
    }

    public void releaseBed(String bedNumber) {
        // Parse bed number e.g. B01
        int num = Integer.parseInt(bedNumber.substring(1));
        int row = (num - 1) / 5;
        int col = (num - 1) % 5;

        if (beds[row][col] == null) {
            throw new IllegalArgumentException("Bed is not occupied.");
        }

        String patientID = beds[row][col];
        Patient p = findPatient(patientID);
        if (p instanceof Inpatient) {
            ((Inpatient) p).setBedNumber(null);
            ((Inpatient) p).setWardNumber(0);
        }
        beds[row][col] = null;
    }

    // Passes a 2D array to a method and uses the length field
    public void displayWardLayout(String[][] bedArray) {
        System.out.println("\nWard Layout:");
        System.out.println("Rows: " + bedArray.length);
        System.out.println("Columns: " + bedArray[0].length);

        for (int row = 0; row < bedArray.length; row++) {
            for (int col = 0; col < bedArray[row].length; col++) {
                String bedNum = "B" + String.format("%02d", (row * 5) + col + 1);
                if (bedArray[row][col] == null) {
                    System.out.print(bedNum + "[ ] ");
                } else {
                    System.out.print(bedNum + "[X] ");
                }
            }
            System.out.println();
        }
    }

    public void displayAvailableBeds() {
        System.out.println("\nAvailable Beds:");
        boolean found= false;
        for (int row = 0; row < beds.length; row++) {
            for (int col = 0; col < beds[row].length; col++) {
                if (beds[row][col] == null) {
                    String bedNum = "B" + String.format("%02d", (row * 5) + col + 1);
                    System.out.print(bedNum + " ");
                    found = true;
                }
            }
        }
        if (!found) {
            System.out.println("No available beds.");
        } else {
            System.out.println();
        }
    }

    public void displayOccupiedBeds() {
        System.out.println("\nOccupied Beds:");
        boolean found = false;
        for (int row = 0; row < beds.length; row++) {
            for (int col = 0; col < beds[row].length; col++) {
                if (beds[row][col] != null) {
                    String bedNum = "B" + String.format("%02d", (row * 5) + col + 1);
                    System.out.println(bedNum + " - Patient: " + beds[row][col]);
                    found = true;
               }
            }
        }
        if (!found) {
            System.out.println("No occupied beds.");
        }
    }

    // ========== REPORTS ==========

    public int getTotalRegisteredPatients() {
        return patients.size();
    }

    public int getTotalOccupiedBeds() {
        int count = 0;
        for (int row = 0; row < beds.length; row++) {
            for (int col = 0; col < beds[row].length; col++) {
                if (beds[row][col] != null) {
                    count++;
               }
            }
        }
        return count;
    }

    public double getWardOccupancyPercentage() {
        int occupied = getTotalOccupiedBeds();
        return (occupied / 20.0) * 100.0;
    }

    public void generateReports() {
        System.out.println("\n=== WARD REPORTS ===");
        System.out.println("Total Registered Patients: " + getTotalRegisteredPatients());
        System.out.println("Total Occupied Beds: " + getTotalOccupiedBeds());
        System.out.println("Ward Occuancy: " + getWardOccupancyPercentage() + "%");
    }

    // ========== SORTING ==========

    // Bubble sort by surname on ArrayList
    public void sortPatientsBySurname() {
        for (int i = 0; i < patients.size() - 1; i++) {
            for (int j = 0; j < patients.size() - i - 1; j++) {
                String surname1 = patients.get(j).getLastName();
                String surname2 = patients.get(j + 1).getLastName();
                if (surname1.compareToIgnoreCase(surname2) > 0) {
                   Patient temp = patients.get(j);
                    patients.set(j, patients.get(j + 1));
                    patients.set(j + 1, temp);
                }
            }
        }
    }

    // Bubble sort by Patient ID on ArrayList
    public void sortPatientsByID() {
        for (int i = 0; i < patients.size() - 1; i++) {
            for (int j = 0; j < patients.size() - i - 1; j++) {
                String id1 = patients.get(j).getPatientID();
                String id2 = patients.get(j + 1).getPatientID();
                if (id1.compareToIgnoreCase(id2) > 0) {
                    Patient temp = patients.get(j);
                    patients.set(j, patients.get(j + 1));
                    patients.set(j + 1, temp);
                }
            }
        }
    }

    // Sort array by surname using bubble sort
    public void displayPatientsSortedArrayBySurname() {
        Patient[] patientArray = new Patient[patients.size()];
        for (int i = 0; i < patients.size(); i++) {
            patientArray[i] = patients.get(i);
        }

        for (int i = 0; i < patientArray.length - 1; i++) {
            for (int j = 0; j < patientArray.length - i - 1; j++) {
                if (patientArray[j].getLastName().compareToIgnoreCase(patientArray[j + 1].getLastName()) > 0) {
                    Patient temp = patientArray[j];
                    patientArray[j] = patientArray[j + 1];
                    patientArray[j + 1] = temp;
                }
            }
        }

        System.out.println("\nPatients sorted by surname (array):");
        for (int i = 0; i < patientArray.length; i++) {
            System.out.println(patientArray[i].getPatientID() + " - "
                    + patientArray[i].getLastName() + ", " + patientArray[i].getFirstName());
        }
    }

    public ArrayList<Patient> getPatients() {
        return patients;
    }

    public String[][] getBeds() {
        return beds;
    }
    }