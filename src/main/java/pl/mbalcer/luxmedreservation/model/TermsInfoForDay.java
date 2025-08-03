package pl.mbalcer.luxmedreservation.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TermsInfoForDay {
    private String day; // ISO date string

    private int termsStatus;

    private String message;

    @JsonProperty("isLimitedDay")
    private boolean isLimitedDay;

    @JsonProperty("isLastDayWithLoadedTerms")
    private boolean isLastDayWithLoadedTerms;

    @JsonProperty("isMoreTermsThanCounter")
    private Boolean isMoreTermsThanCounter; // Can be null

    private TermsCounter termsCounter;
}