package com.userservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicUpdate
@Table (name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Column (name = "name")
    private String name;
    @Column (name = "surname")
    private String surname;
    @Column (name = "birth_date")
    private LocalDate birthDate;
    @Column (name = "email")
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CardInfo> cards;

    @Version
    private Long version;
}
