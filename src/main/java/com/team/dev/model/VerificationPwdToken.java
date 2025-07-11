package com.team.dev.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
public class VerificationPwdToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Date expirationToken;
    private static final  int EXPIRATION_TIME = 15;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public VerificationPwdToken(String token, User user) {
        this.token = token;
        this.expirationToken = this.getTokenExpirationTime();
        this.user = user;
    }
    public VerificationPwdToken(User user) {
        this.expirationToken = this.getTokenExpirationTime();
        this.user = user;
    }
    public VerificationPwdToken(String token) {
        this.token = token;
        this.expirationToken = this.getTokenExpirationTime();
    }




    public  Date getTokenExpirationTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
        return  new Date(calendar.getTime().getTime());
    }
}
