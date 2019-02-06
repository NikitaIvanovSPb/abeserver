package ru.nikita.abeserver.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "tokens")
public class Token {

    @Id
    private String guid;

    @Column(nullable = false)
    private String attrbutes;

    @Column(nullable = false)
    private boolean used;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date create;

    @Column()
    @Temporal(TemporalType.TIMESTAMP)
    private Date generateDate;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String key;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
