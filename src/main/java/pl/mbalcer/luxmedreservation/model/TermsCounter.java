package pl.mbalcer.luxmedreservation.model;

import java.util.List;

public record TermsCounter(
        int termsNumber,
        List<PartialTermsCounter> partialTermsCounters
) {
}