package pl.mbalcer.luxmedreservation.term;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.mbalcer.luxmedreservation.authorization.UserCredential;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "term_search")
public class TermSearch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_credential_id")
    private UserCredential user;

    @Column(nullable = false)
    private Integer cityId;

    @Column(nullable = false)
    private Long serviceVariantId;

    @ElementCollection
    @CollectionTable(
            name = "term_search_doctor_ids",
            joinColumns = @JoinColumn(name = "term_search_id")
    )
    @Column(name = "doctor_id")
    private Set<Long> doctorIds = new HashSet<>();

    @Column(nullable = false)
    private LocalDate dateFrom;

    @Column(nullable = false)
    private LocalDate dateTo;

    @Column(nullable = false)
    private boolean delocalized = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TermSearchStatus status = TermSearchStatus.SEARCHING;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime lastCheckedAt;
    private OffsetDateTime lastFoundAt;

    @PrePersist
    void prePersist() {
        createdAt = OffsetDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void preUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}
