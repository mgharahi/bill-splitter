package com.snapp.billsplitter.infrastructure.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name = "users"
        , indexes = {
        @Index(name = "idx_users_username", columnList = "username", unique = true)}
)
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<EventEntity> events;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private WalletEntity wallet;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<TransactionEntity> transactions;
}