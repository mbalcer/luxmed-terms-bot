package pl.mbalcer.luxmedreservation.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// Terms for service class
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TermsForService {
    private int serviceVariantId;
    private AdditionalData additionalData;
    private Object termsForDays; // This appears to be an array but not shown in the JSON
    private List<TermsInfoForDay> termsInfoForDays;
}