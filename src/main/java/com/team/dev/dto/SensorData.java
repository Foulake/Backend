package com.team.dev.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SensorData {
    private Long id;

    private double soilMoisture;
    private double temperature;

    private LocalDateTime timestamp;
}
