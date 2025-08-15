package pl.mbalcer.luxmedreservation.term;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringTermSearchRepository extends TermSearchRepository, JpaRepository<TermSearch, Long> {
}
