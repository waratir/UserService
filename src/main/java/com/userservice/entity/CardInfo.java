package com.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "card_info")
public class CardInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
    @Column(name = "number")
    private String number;
    @Column(name = "holder")
    private String holder;
    @Column (name = "expiration_date")
    private LocalDate expirationDate;

}
