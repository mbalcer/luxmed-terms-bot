package pl.mbalcer.luxmedreservation.authorization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringUserCredentialRepository extends UserCredentialRepository, JpaRepository<UserCredential, Long> {
}
