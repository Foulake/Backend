package com.team.dev.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChangerPaswword {

    private String currentPassword;
    private String newPassword;
    private String confirmationPassword;
}
