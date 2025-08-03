package pl.mbalcer.luxmedreservation.model;

public record PartialTermsCounter(
        int clinicGroupId,
        int doctorId,
        int priority,
        int termsNumber
) {
}
