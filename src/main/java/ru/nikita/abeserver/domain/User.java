package ru.nikita.abeserver.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    @NotNull
    private String login;
    @Column(nullable = false)
    @NotNull
    private String password;
    @Column(name = "registration_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;
    @Column
    private String name;
    @Column(name = "last_name")
    private String lastName;
    @Column
    private String patronymic;
    @Column(nullable = false)
    @NotNull
    @Email
    private String email;
    @Column(nullable = false)
    private Boolean admin;


    @Transient
    private String confirmPassword;

    @ManyToMany
    @JoinTable(name="users_roles", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany
    @JoinTable(name="users_groups", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    private Set<Group> groups = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    Set<Token> tokens = new HashSet<>();
}
