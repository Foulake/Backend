package com.team.dev.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "recoltes")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Recolte extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    //@NotBlank(message = "La date de debut est requise !")
    @NotNull(message = "La date de début est réquise !")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @NotNull(message = "La quatité est réquise !")
    private BigDecimal quantite;

    @Column(name = "typeCulture")
    private String typeCulture;

    @Column(name = "uniteMesure")
    private String uniteMesure;

    @Column(name = "qualite")
    private String qualite;

//    @NotBlank(message = "La condition Météorologique est requise !")
//    @Column(name = "conditioMeteo")
//    private String conditioMeteo;
    @Column(name = "imageUrl")
    private String imageUrl;
//    @Column(name = "description")
//    private String desc;
    @Column(name = "entrepriseId")
    private Long entrepriseId;

    @ManyToOne
    private Champ Champ;

}
