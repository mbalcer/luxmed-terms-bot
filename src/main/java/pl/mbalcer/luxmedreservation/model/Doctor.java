package pl.mbalcer.luxmedreservation.model;

public record Doctor(
        int id,
        int genderId,
        String academicTitle,
        String firstName,
        String lastName
) {
    public String getFullName() {
        return (academicTitle != null ? academicTitle + " " : "") + firstName + " " + lastName;
    }
}