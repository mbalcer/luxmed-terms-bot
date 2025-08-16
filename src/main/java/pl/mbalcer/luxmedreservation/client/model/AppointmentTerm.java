package pl.mbalcer.luxmedreservation.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AppointmentTerm(
        String dateTimeFrom,
        String dateTimeTo,
        Doctor doctor,
        int clinicId,
        String clinic,
        int clinicGroupId,
        String clinicGroup,
        int roomId,
        int serviceId,
        long scheduleId,
        @JsonProperty("isImpediment") boolean isImpediment,
        String impedimentText,
        @JsonProperty("isAdditional") boolean isAdditional,
        @JsonProperty("isTelemedicine") boolean isTelemedicine,
        @JsonProperty("isInfectionTreatmentCenter") boolean isInfectionTreatmentCenter,
        int partOfDay,
        int priority
) {
}
