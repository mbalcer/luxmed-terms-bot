package pl.mbalcer.luxmedreservation.term;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringTermSearchRepository extends TermSearchRepository, JpaRepository<TermSearch, Long> {

    @Query("""
            SELECT ts
            FROM TermSearch ts
            JOIN FETCH ts.user
            LEFT JOIN FETCH ts.doctorIds
            WHERE ts.status = :status
            """)
    List<TermSearch> findAllByStatus(@Param("status") TermSearchStatus status);

}
