package pl.mbalcer.luxmedreservation.client.model;

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