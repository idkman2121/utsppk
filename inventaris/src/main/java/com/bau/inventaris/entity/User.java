package com.bau.inventaris.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String nim;

    @Column(nullable = false)
    private String kelas;

    @Column(nullable = false)
    private String password;
    
    @Transient 
    private String token;

}
