package com.pizzurg.api.entity;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "users")
@Entity(name = "User")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;

}
