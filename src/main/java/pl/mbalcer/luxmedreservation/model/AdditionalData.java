package pl.mbalcer.luxmedreservation.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AdditionalData(
        @JsonProperty("isPreparationRequired") boolean isPreparationRequired,
        List<Object> preparationItems,
        @JsonProperty("previousTermsAvailable") boolean previousTermsAvailable,
        @JsonProperty("nextTermsAvailable") boolean nextTermsAvailable,
        @JsonProperty("anyTermForTelemedicine") boolean anyTermForTelemedicine,
        @JsonProperty("anyTermForFacilityVisit") boolean anyTermForFacilityVisit
) {
}
