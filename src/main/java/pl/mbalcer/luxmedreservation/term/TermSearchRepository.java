package pl.mbalcer.luxmedreservation.term;

import java.time.LocalDate;
import java.util.List;

public interface TermSearchRepository {
    List<TermSearch> findAllByUser_Email(String email);

    boolean existsByUser_EmailAndCityIdAndServiceVariantIdAndDateFromAndDateToAndDelocalized(
            String email, Integer cityId, Long serviceVariantId, LocalDate dateFrom, LocalDate dateTo, boolean delocalized
    );

    TermSearch save(TermSearch termSearch);
}
