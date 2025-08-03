package pl.mbalcer.luxmedreservation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalAppointmentResponse {
    private String correlationId;
    private TermsForService termsForService;
    private int pMode;
    private boolean success;
}