package pl.mbalcer.luxmedreservation.model;

import java.util.List;

public record TermsForService(
        int serviceVariantId,
        AdditionalData additionalData,
        List<TermsForDay> termsForDays,
        List<TermsInfoForDay> termsInfoForDays
) {
}