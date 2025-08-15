package pl.mbalcer.luxmedreservation.term;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;

public record TermSearchDTO(
        Long id,
        Integer cityId,
        Long serviceVariantId,
        Set<Long> doctorIds,
        LocalDate dateFrom,
        LocalDate dateTo,
        boolean delocalized,
        TermSearchStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        OffsetDateTime lastCheckedAt,
        OffsetDateTime lastFoundAt
) {}

