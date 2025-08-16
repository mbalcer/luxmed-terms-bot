package pl.mbalcer.luxmedreservation.client.model;

public record MedicalAppointmentResponse(
        String correlationId,
        TermsForService termsForService,
        int pMode,
        boolean success
) {
}
