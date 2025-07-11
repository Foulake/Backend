package com.team.dev.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecolteResponse {

    private Long id;
    //private Long PlancheId;
    private Long ChampId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private BigDecimal quantite;
    private String typeCulture;
    private String uniteMesure;
    private String qualite;
    private String imageUrl;
    private String conditioMeteo;

    //private String desc;

    private String champ;
    private Long entrepriseId;

    /*private List<RecolteRequest> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;*/
}
