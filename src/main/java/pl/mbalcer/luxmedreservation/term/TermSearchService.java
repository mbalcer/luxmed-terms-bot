package pl.mbalcer.luxmedreservation.term;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.mbalcer.luxmedreservation.authorization.UserCredential;
import pl.mbalcer.luxmedreservation.authorization.UserCredentialRepository;
import pl.mbalcer.luxmedreservation.error.exception.DuplicateTermSearchException;
import pl.mbalcer.luxmedreservation.error.exception.InvalidDateRangeException;
import pl.mbalcer.luxmedreservation.error.exception.UserNotFoundException;

import java.time.LocalDate;
import java.util.List;

@Service
public class TermSearchService {

    private final TermSearchRepository repo;
    private final UserCredentialRepository userRepo;

    public TermSearchService(TermSearchRepository repo, UserCredentialRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    @Transactional
    public TermSearch create(String userEmail, CreateTermSearchRequest req) {
        UserCredential user = userRepo.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException(userEmail));

        validateDates(req.dateFrom(), req.dateTo());

        boolean existSearchTerm = repo.existsByUser_EmailAndCityIdAndServiceVariantIdAndDateFromAndDateToAndDelocalized(
                userEmail, req.cityId(), req.serviceVariantId(), req.dateFrom(), req.dateTo(), req.delocalized() != null && req.delocalized()
        );
        if (existSearchTerm) {
            throw new DuplicateTermSearchException();
        }

        TermSearch termSearch = TermSearchMapper.fromCreateDto(req);
        termSearch.setUser(user);
        termSearch.setStatus(TermSearchStatus.SEARCHING);

        return repo.save(termSearch);
    }

    @Transactional(readOnly = true)
    public List<TermSearch> listForUser(String userEmail) {
        return repo.findAllByUser_Email(userEmail);
    }

    @Transactional(readOnly = true)
    public List<TermSearchDTO> listSearching() {
        return repo.findAllByStatus(TermSearchStatus.SEARCHING)
                .stream()
                .map(TermSearchMapper::toDto)
                .toList();
    }

    private static void validateDates(LocalDate from, LocalDate to) {
        if (to.isBefore(from)) {
            throw new InvalidDateRangeException();
        }
    }
}
