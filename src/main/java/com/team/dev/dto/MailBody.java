package com.team.dev.dto;


import lombok.Builder;
@Builder
public record MailBody (String to,String text,String subject ){

}
