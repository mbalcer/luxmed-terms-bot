package pl.mbalcer.luxmedreservation.model;

public record MedicalAppointmentResponse(
        String correlationId,
        TermsForService termsForService,
        int pMode,
        boolean success
) {
}
