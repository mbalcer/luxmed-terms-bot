package pl.mbalcer.luxmedreservation.authorization;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
public class UserCredentialService {

    private final UserCredentialRepository repo;
    private final EncryptionService encryptionService;

    public UserCredentialService(UserCredentialRepository repo, EncryptionService encryptionService) {
        this.repo = repo;
        this.encryptionService = encryptionService;
    }

    @Transactional
    public UserCredential save(String email, String plainPassword) {
        String encrypted = encryptionService.encrypt(plainPassword);
        Optional<UserCredential> existing = repo.findByEmail(email);
        if (existing.isPresent()) {
            UserCredential user = existing.get();
            user.setPasswordEncrypted(encrypted);
            user.setUpdatedAt(OffsetDateTime.now());
            return repo.save(user);
        } else {
            return repo.save(new UserCredential(email, encrypted));
        }
    }
}

