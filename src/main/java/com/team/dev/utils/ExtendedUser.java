package com.team.dev.utils;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;


public class ExtendedUser extends User {

    @Getter
    @Setter
    private Long entrepriseId;

    public ExtendedUser(String email,
                        String password,
                        Collection< ?extends GrantedAuthority > authorities) {
        super(email, password, authorities);
    }
    public ExtendedUser(String email,
                        String password,
                        Collection<?extends GrantedAuthority> authorities,
                        Long entrepriseId) {
        super(email, password, authorities);
        this.entrepriseId = entrepriseId;
    }
}
