package com.team.dev.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;



public record ForgotPwd(String password, String repeatPassword) {
}
