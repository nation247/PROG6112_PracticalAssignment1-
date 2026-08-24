import java.util.Scanner;

// Main console application
public class HospitalSystem {
    private static final HospitalManager manager = new HospitalManager();
    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  MEDI-CARE HOSPITAL PATIENT SYSTEM");
        System.out.println("========================================");

        boolean running = true;

       while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Patient Management");
            System.out.println("2. Bed Management");
            System.out.println("3. Reports");
            System.out.println("4. Sorting");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            String choice = input.nextLine();

            try {
                if (choice.equals("1")) {
                    patientMenu();
                } else if (choice.equals("2")) {
                    bedMenu();
                } else if (choice.equals("3")) {
                    reportsMenu();
                } else if (choice.equals("4")) {
                    sortingMenu();
                } else if (choice.equals("0")) {
                    System.out.println("Goodbye!");
                    running = false;
                } else {
                    System.out.println("Invalid choice. Try again.");
                }
           } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        input.close();
    }

    public static void patientMenu() {
        System.out.println("\n--- PATIENT MANAGEMENT ---");
        System.out.println("1. Register Patient");
        System.out.println("2. Search Patient");
        System.out.println("3. Update Patient");
        System.out.println("4. Delete Patient");
        System.out.println("5. Display All Patients");
        System.out.print("Enter choice: ");

        String choice = input.nextLine();

        try {
            if (choice.equals("1")) {
                registerPatient();
            } else if (choice.equals("2")) {
                searchPatient();
            } else if (choice.equals("3")) {
                updatePatient();
            } else if (choice.equals("4")) {
                deletePatient();
            } else if (choice.equals("5")) {
                manager.displayAllPatients();
            } else {
               System.out.println("Invalid choice.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void registerPatient() {
        System.out.print("Enter Patient ID: ");
        String id = input.nextLine();

        System.out.print("Enter First Name: ");
        String fname = input.nextLine();

        System.out.print("Enter Last Name: ");
        String lname = input.nextLine();

        System.out.print("Enter Age: ");
        int age = Integer.parseInt(input.nextLine());

        System.out.print("Enter Gender: ");
        String gender = input.nextLine();

        System.out.print("Enter Medical Condition: ");
        String condition = input.nextLine();

        System.out.println("Select Category:");
        System.out.println("1. Inpatient");
        System.out.println("2. Outpatient");
        System.out.println("3. Emergency");
        System.out.print("Choice: ");
        String cat = input.nextLine();

        Patient patient;

        if (cat.equals("1")) {
            patient = new Inpatient(id, fname, lname, age, gender, condition, 0, null);
        } else if (cat.equals("2")) {
            patient = new Patient(id, fname, lname, age, gender, condition, PatientCategory.OUTPATIENT);
        } else if (cat.equals("3")) {
            patient = new Patient(id, fname, lname, age, gender, condition, PatientCategory.EMERGENCY);
        } else {
            System.out.println("Invalid category.");
            return;
        }

        manager.registerPatient(patient);
        System.out.println("Patient registered successfully.");
    }

    public static void searchPatient() {
        System.out.print("Enter Patient ID: ");
        String id = input.nextLine();

        Patient p = (Patient) manager.findPatient(id);
        if (p != null) {
            p.displayDetails();
        } else {
            System.out.println("Patient not found.");
        }
    }

    public static void updatePatient() {
       System.out.print("Enter Patient ID to update: ");
        String id = input.nextLine();

        if (manager.findPatient(id) == null) {
            throw new IllegalArgumentException("Patient not found.");
        }

        System.out.print("Enter new First Name: ");
        String fname = input.nextLine();

        System.out.print("Enter new Last Name: ");
        String lname = input.nextLine();

        System.out.print("Enter new Age: ");
        int age = Integer.parseInt(input.nextLine());

       System.out.print("Enter new Gender: ");
        String gender = input.nextLine();

        System.out.print("Enter new Medical Condition: ");
        String condition = input.nextLine();

        manager.updatePatient(id, fname, lname, age, gender, condition);
        System.out.println("Patient updated successfully.");
    }

    public static void deletePatient() {
        System.out.print("Enter Patient ID to delete: ");
        String id = input.nextLine();

        manager.deletePatient(id);
       System.out.println("Patient deleted successfully.");
    }

    public static void bedMenu() {
        System.out.println("\n--- BED MANAGEMENT ---");
        System.out.println("1. Allocate Bed");
        System.out.println("2. Release Bed");
        System.out.println("3. Display Ward Layout");
        System.out.println("4. Display Available Beds");
        System.out.println("5. Display Occupied Beds");
        System.out.print("Enter choice: ");

        String choice = input.nextLine();

       try {
            if (choice.equals("1")) {
                allocateBed();
            } else if (choice.equals("2")) {
                releaseBed();
            } else if (choice.equals("3")) {
                manager.displayWardLayout(manager.getBeds());
            } else if (choice.equals("4")) {
                manager.displayAvailableBeds();
            } else if (choice.equals("5")) {
                manager.displayOccupiedBeds();
            } else {
                System.out.println("Invalid choice.");
           }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void allocateBed() {
        System.out.print("Enter Inpatient ID: ");
        String id = input.nextLine();

        String bed = manager.allocateBed(id);
        System.out.println("Bed " + bed + " allocated successfully.");
    }

    public static void releaseBed() {
        System.out.print("Enter Bed Number (e.g. B01): ");
        String bed = input.nextLine().trim().toUpperCase();

        manager.releaseBed(bed);
        System.out.println("Bed " + bed + " released successfully.");
    }

    public static void reportsMenu() {
        System.out.println("\n--- REPORTS ---");
        System.out.println("1. All Registered Patients");
        System.out.println("2. All Available Beds");
        System.out.println("3. All Occupied Beds");
        System.out.println("4. Summary Statistics");
        System.out.print("Enter choice: ");

        String choice = input.nextLine();

        if (choice.equals("1")) {
            manager.displayAllPatients();
        } else if (choice.equals("2")) {
            manager.displayAvailableBeds();
        } else if (choice.equals("3")) {
            manager.displayOccupiedBeds();
        } else if (choice.equals("4")) {
            manager.generateReports();
        } else {
            System.out.println("Invalid choice.");
        }
    }

    public static void sortingMenu() {
        System.out.println("\n--- SORTING ---");
       System.out.println("1. Sort by Surname (ArrayList)");
        System.out.println("2. Sort by Patient ID (ArrayList)");
        System.out.println("3. Sort by Surname (Array)");
        System.out.print("Enter choice: ");

        String choice = input.nextLine();

        if (choice.equals("1")) {
            manager.sortPatientsBySurname();
            System.out.println("Sorted by surname.");
            manager.displayAllPatients();
        } else if (choice.equals("2")) {
            manager.sortPatientsByID();
            System.out.println("Sorted by ID.");
            manager.displayAllPatients();
        } else if (choice.equals("3")) {
            manager.displayPatientsSortedArrayBySurname();
        } else {
            System.out.println("Invalid choice.");
        }
    }   
}
