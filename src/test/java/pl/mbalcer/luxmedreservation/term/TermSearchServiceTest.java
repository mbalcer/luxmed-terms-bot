package pl.mbalcer.luxmedreservation.term;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.mbalcer.luxmedreservation.authorization.UserCredential;
import pl.mbalcer.luxmedreservation.util.InMemoryTermSearchRepository;
import pl.mbalcer.luxmedreservation.util.InMemoryUserCredentialRepository;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TermSearchServiceTest {

    private InMemoryTermSearchRepository termRepo;
    private TermSearchService service;

    @BeforeEach
    void setUp() {
        InMemoryUserCredentialRepository userRepo = new InMemoryUserCredentialRepository();
        termRepo = new InMemoryTermSearchRepository();
        service = new TermSearchService(termRepo, userRepo);

        UserCredential user = new UserCredential();
        user.setEmail("test@example.com");
        userRepo.save(user);
    }

    @Test
    void shouldCreateNewTermSearch() {
        CreateTermSearchRequest req = new CreateTermSearchRequest(
                1,
                100L,
                List.of(),
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                true
        );

        TermSearch result = service.create("test@example.com", req);

        assertThat(result.getId()).isNotNull();
        assertThat(result.getUser().getEmail()).isEqualTo("test@example.com");
        assertThat(result.getStatus()).isEqualTo(TermSearchStatus.SEARCHING);
        assertThat(termRepo.findAllByUser_Email("test@example.com")).hasSize(1);
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        CreateTermSearchRequest req = new CreateTermSearchRequest(
                1, 100L, List.of(), LocalDate.now(), LocalDate.now().plusDays(1), false
        );

        assertThatThrownBy(() -> service.create("missing@example.com", req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void shouldThrowWhenDateToBeforeDateFrom() {
        CreateTermSearchRequest req = new CreateTermSearchRequest(
                1, 100L, List.of(), LocalDate.now(), LocalDate.now().minusDays(1), false
        );

        assertThatThrownBy(() -> service.create("test@example.com", req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("dateTo cannot be before dateFrom");
    }

    @Test
    void shouldThrowWhenSuchSearchAlreadyExists() {
        CreateTermSearchRequest req = new CreateTermSearchRequest(
                1, 100L, List.of(), LocalDate.now(), LocalDate.now().plusDays(5), true
        );

        service.create("test@example.com", req);

        assertThatThrownBy(() -> service.create("test@example.com", req))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Such search already exists");
    }
}