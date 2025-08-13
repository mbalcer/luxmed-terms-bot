package pl.mbalcer.luxmedreservation.authorization;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.mbalcer.luxmedreservation.util.InMemoryUserCredentialRepository;

import java.time.OffsetDateTime;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

public class UserCredentialServiceTest {
    private UserCredentialService service;
    private InMemoryUserCredentialRepository repo;
    private EncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        repo = new InMemoryUserCredentialRepository();
        encryptionService = new EncryptionService(Base64.getEncoder().encodeToString(new byte[32]));
        service = new UserCredentialService(repo, encryptionService);
    }

    @Test
    void shouldSaveNewUserWithEncryptedPassword() {
        String email = "test@example.com";
        String password = "Secret123!";

        UserCredential saved = service.save(email, password);

        assertThat(saved.getEmail()).isEqualTo(email);
        assertThat(saved.getPasswordEncrypted()).isNotEqualTo(password);
        assertThat(encryptionService.decrypt(saved.getPasswordEncrypted())).isEqualTo(password);
        assertThat(repo.findByEmail(email)).isPresent();
    }

    @Test
    void shouldUpdateExistingUserPasswordAndUpdatedAt() throws InterruptedException {
        String email = "test@example.com";
        String oldPass = "OldPass";
        String newPass = "NewPass";

        UserCredential firstSave = service.save(email, oldPass);
        OffsetDateTime firstUpdatedAt = firstSave.getUpdatedAt();
        String oldEncryptedPassword = firstSave.getPasswordEncrypted();

        Thread.sleep(5);

        UserCredential updated = service.save(email, newPass);

        assertThat(updated.getPasswordEncrypted()).isNotEqualTo(oldEncryptedPassword);
        assertThat(encryptionService.decrypt(updated.getPasswordEncrypted())).isEqualTo(newPass);
        assertThat(updated.getUpdatedAt()).isAfter(firstUpdatedAt);
        assertThat(repo.findByEmail(email)).isPresent();
    }

}
