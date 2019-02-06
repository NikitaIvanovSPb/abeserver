package ru.nikita.abeserver.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class FTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String url;
    @Column(nullable = false)
    Integer port;
    @Column(nullable = false)
    String userLogin;
    @Column(nullable = false)
    String userPass;
    @Column(nullable = false)
    String adminLogin;
    @Column(nullable = false)
    String adminPass;
    @Column(nullable = false)
    Boolean deleted;

    @OneToMany(mappedBy = "ftp", cascade = CascadeType.ALL)
    Set<File> files = new HashSet<>();
}
