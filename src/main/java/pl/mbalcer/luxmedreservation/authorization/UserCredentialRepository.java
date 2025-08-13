package pl.mbalcer.luxmedreservation.authorization;

import java.util.Optional;

public interface UserCredentialRepository {
    Optional<UserCredential> findByEmail(String email);

    UserCredential save(UserCredential userCredential);
}
