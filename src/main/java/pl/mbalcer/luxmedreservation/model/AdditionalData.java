package pl.mbalcer.luxmedreservation.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalData {
    @JsonProperty("isPreparationRequired")
    private boolean isPreparationRequired;

    private List<Object> preparationItems; // Array of unknown type

    @JsonProperty("previousTermsAvailable")
    private boolean previousTermsAvailable;

    @JsonProperty("nextTermsAvailable")
    private boolean nextTermsAvailable;

    @JsonProperty("anyTermForTelemedicine")
    private boolean anyTermForTelemedicine;

    @JsonProperty("anyTermForFacilityVisit")
    private boolean anyTermForFacilityVisit;
}