package pl.mbalcer.luxmedreservation.term;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.List;

public record CreateTermSearchRequest(
        @NotNull Integer cityId,
        @NotNull @Positive Long serviceVariantId,
        List<Long> doctorIds,
        @NotNull LocalDate dateFrom,
        @NotNull LocalDate dateTo,
        Boolean delocalized
) { }