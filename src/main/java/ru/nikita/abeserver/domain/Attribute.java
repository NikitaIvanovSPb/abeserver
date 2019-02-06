package ru.nikita.abeserver.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String engName;
    @Column(nullable = false)
    private String rusName;
    @Column(nullable = false)
    private String valName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;


    public Attribute(){}

    public Attribute(String valName, Type type, String engName, String rusName) {
        this.type = type;
        this.engName = engName;
        this.rusName = rusName;
        this.valName = valName;
    }

    public enum Type{
        Organization,
        Group,
        User
    }
}
