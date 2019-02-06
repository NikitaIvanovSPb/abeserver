package ru.nikita.abeserver.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "abe_keys")
public class ABEKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "public_key", nullable = false, columnDefinition = "TEXT")
    private String publicKey;
    @Column(name = "secret_key", nullable = false, columnDefinition = "TEXT")
    private String secretKey;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date create;
    @Column(nullable = false)
    private Boolean active;

}
