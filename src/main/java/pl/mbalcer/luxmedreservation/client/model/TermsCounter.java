package pl.mbalcer.luxmedreservation.client.model;

import java.util.List;

public record TermsCounter(
        int termsNumber,
        List<PartialTermsCounter> partialTermsCounters
) {
}