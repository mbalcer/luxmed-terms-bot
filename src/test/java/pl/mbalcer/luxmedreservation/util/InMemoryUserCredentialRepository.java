package pl.mbalcer.luxmedreservation.util;

import pl.mbalcer.luxmedreservation.authorization.UserCredential;
import pl.mbalcer.luxmedreservation.authorization.UserCredentialRepository;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserCredentialRepository implements UserCredentialRepository {
    private final Map<String, UserCredential> memory = new HashMap<>();
    private long idSequence = 1;

    @Override
    public Optional<UserCredential> findByEmail(String email) {
        return Optional.ofNullable(memory.get(email));
    }

    @Override
    public UserCredential save(UserCredential userCredential) {
        if (userCredential.getId() == null) {
            userCredential.setId(idSequence++);
            userCredential.setCreatedAt(OffsetDateTime.now());
        }
        memory.put(userCredential.getEmail(), userCredential);
        return userCredential;
    }
}
