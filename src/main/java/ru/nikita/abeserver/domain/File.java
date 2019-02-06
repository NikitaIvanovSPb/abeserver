package ru.nikita.abeserver.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "files")
public class File {
    @Id
    private String guid;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String attributes;
    @Column(name = "AES_key_base64", columnDefinition = "TEXT", nullable = false)
    private String AESKeyBase64;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date create;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private FTP ftp;

    @ManyToMany
    @JoinTable(name="files_groups", joinColumns = @JoinColumn(name = "file_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    private Set<Group> groups = new HashSet<>();

}
