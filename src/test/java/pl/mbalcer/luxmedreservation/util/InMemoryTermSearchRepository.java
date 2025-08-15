package pl.mbalcer.luxmedreservation.util;

import pl.mbalcer.luxmedreservation.term.TermSearch;
import pl.mbalcer.luxmedreservation.term.TermSearchRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTermSearchRepository implements TermSearchRepository {
    private final Map<Long, TermSearch> storage = new HashMap<>();
    private long idSeq = 1L;

    @Override
    public List<TermSearch> findAllByUser_Email(String email) {
        return storage.values().stream()
                .filter(t -> t.getUser().getEmail().equals(email))
                .toList();
    }

    @Override
    public boolean existsByUser_EmailAndCityIdAndServiceVariantIdAndDateFromAndDateToAndDelocalized(
            String email, Integer cityId, Long serviceVariantId, LocalDate dateFrom, LocalDate dateTo, boolean delocalized
    ) {
        return storage.values().stream().anyMatch(t ->
                t.getUser().getEmail().equals(email) &&
                        t.getCityId().equals(cityId) &&
                        t.getServiceVariantId().equals(serviceVariantId) &&
                        t.getDateFrom().equals(dateFrom) &&
                        t.getDateTo().equals(dateTo) &&
                        t.isDelocalized() == delocalized
        );
    }

    @Override
    public TermSearch save(TermSearch termSearch) {
        if (termSearch.getId() == null) {
            termSearch.setId(idSeq++);
        }
        storage.put(termSearch.getId(), termSearch);
        return termSearch;
    }
}
