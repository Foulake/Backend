package com.team.dev.dto;

import com.team.dev.model.SousChamp;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecolteRequest {
    private Long id;
    //private Long PlancheId;
    private Long ChampId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    //@NotBlank(message = "La date de debut est réquise !")
    @Column(nullable = false)
    @NotNull(message = "La date de début est réquise !")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    //@NotBlank(message = "La date de fin est réquise !")
    private Date endDate;
    private String typeCulture;
    private String uniteMesure;

    private String qualite;
//    @NotBlank(message = "La condition Météorologique est requise !")
//    private String conditioMeteo;
    @NotNull(message = "La quatité est réquise !")
    private BigDecimal quantite;
    private String imageUrl;
    //private String desc;

    private String champ;
    private Long entrepriseId;
}
