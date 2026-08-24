// Enum to represent the patient category
// Demonstrates: Apply Enum methods
public enum PatientCategory {
    INPATIENT("Requires a hospital bed"),
    OUTPATIENT("Does not require a bed"),
    EMERGENCY("Immediate treatment needed");

    private String description;

    // Enum constructor
    PatientCategory(String description) {
        this.description = description;
    }

    // Enum method
    public String getDescription() {
        return description;
    }

    // Uses built-in enum methods:values() and name()
    public static void displayCategories() {
        System.out.println("Available Categories:");
        for (PatientCategory category : PatientCategory.values()) {
            System.out.println("- " + category.name() + ": " + category.getDescription());
        }
    }
}
