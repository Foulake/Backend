package com.team.dev.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean modeAuto;
    private int seuil;

    private LocalDateTime timestamp;  // Date et heure de la modification

    public ConfigHistory(boolean modeAuto, int seuil, LocalDateTime timestamp) {
        this.modeAuto = modeAuto;
        this.seuil = seuil;
        this.timestamp = timestamp;
    }
}
