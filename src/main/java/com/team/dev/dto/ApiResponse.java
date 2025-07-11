package com.team.dev.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponse {
	
	private Boolean success;
	private String message;

    public ApiResponse(String message) {
        this.message = message;
    }

    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
