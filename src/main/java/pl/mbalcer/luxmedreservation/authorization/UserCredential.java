package pl.mbalcer.luxmedreservation.authorization;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "user_credentials", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class UserCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_encrypted", nullable = false, length = 1024)
    @ToString.Exclude
    private String passwordEncrypted;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public UserCredential(String email, String passwordEncrypted) {
        this.email = email;
        this.passwordEncrypted = passwordEncrypted;
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }
}