package com.team.dev.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ForgotPassword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fpid;
    @Column(nullable = false)
    private int otp;
    @Column(nullable = false)

    private Date expirationTime;
    private static final  int EXPIRATION_TIME = 5;

    @OneToOne
    @JoinColumn(name = "user")
    private User user;
}
