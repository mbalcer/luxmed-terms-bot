package pl.mbalcer.luxmedreservation.model;

import java.util.List;

public record TermsForDay(
        String day,
        String correlationId,
        List<AppointmentTerm> terms
) {
}