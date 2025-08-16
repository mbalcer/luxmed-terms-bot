package pl.mbalcer.luxmedreservation.client.model;

public record PartialTermsCounter(
        int clinicGroupId,
        int doctorId,
        int priority,
        int termsNumber
) {
}
