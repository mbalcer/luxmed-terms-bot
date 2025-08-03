package pl.mbalcer.luxmedreservation.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TermsInfoForDay(
        String day,
        int termsStatus,
        String message,
        @JsonProperty("isLimitedDay") boolean isLimitedDay,
        @JsonProperty("isLastDayWithLoadedTerms") boolean isLastDayWithLoadedTerms,
        @JsonProperty("isMoreTermsThanCounter") Boolean isMoreTermsThanCounter,
        TermsCounter termsCounter
) {
}
