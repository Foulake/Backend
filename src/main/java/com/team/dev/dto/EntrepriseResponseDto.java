package com.team.dev.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
public class EntrepriseResponseDto {

    private Long idEntreprise;
    private String nom;
    private String adresse;
    private String email;
    private String contact;
    private String description;
    private String imageUrl;
    private String userNames;

    private List<EntrepriseRequestDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;

}
