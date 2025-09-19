package com.snapp.billsplitter.infrastructure.spring.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "events")
@Getter
@Setter
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title")
    private String title;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<UserEntity> users;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<TransactionEntity> transactions;
}