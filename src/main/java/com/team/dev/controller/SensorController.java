package com.team.dev.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.team.dev.dto.ConfigData;
import com.team.dev.dto.SensorData;
import com.team.dev.exception.ApiException;
import com.team.dev.model.ConfigHistory;
import com.team.dev.model.SensorDataEntity;
import com.team.dev.repository.ConfigHistoryRepository;
import com.team.dev.repository.SensorDataRepository;
import com.team.dev.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/sensor")
@RequiredArgsConstructor
public class SensorController {


    private boolean modeAuto = true;
    private int seuil = 20;

    final ObjectMapper mapper = new ObjectMapper();


    private static final String ESP8266_URL = "http://192.168.244.10:8182/api/v1/sensor/config";

    private final RestTemplate restTemplate;

    private final AlertService alertService;
    private final SensorDataRepository sensorDataRepository;
    private final ConfigHistoryRepository configHistoryRepository;

    /*@PostMapping
    public ResponseEntity<String> receiveSensorData(@RequestBody SensorData sensorData) {
        double soilMoisture = sensorData.getSoilMoisture();
        double temperature = sensorData.getTemperature();

        System.out.println("Données reçues - Humidité: " + soilMoisture + "%, Température: " + temperature + "°C");

        if (modeAuto && soilMoisture < seuil) {
            alertService.sendLowMoistureAlert();
            return ResponseEntity.ok("ACTIVATE_PUMP");
        }

        // Sauvegarde des données dans la base de données
        SensorDataEntity entity = new SensorDataEntity();
        entity.setSoilMoisture(soilMoisture);
        entity.setTemperature(sensorData.getTemperature());
        entity.setTimestamp(LocalDateTime.now());

        sensorDataRepository.save(entity);
        return ResponseEntity.ok("NO_ACTION");
    }
*/

    @PostMapping
    public ResponseEntity<String> receiveSensorData(@RequestBody SensorData sensorData) {
        try {
            double soilMoisture = sensorData.getSoilMoisture();
            double temperature = sensorData.getTemperature();

            System.out.println("Données reçues - Humidité: " + soilMoisture + "%, Température: " + temperature + "°C");

            if (modeAuto && soilMoisture < seuil) {
                alertService.sendLowMoistureAlert();
                return ResponseEntity.ok("ACTIVATE_PUMP");
            }

            saveSensorData(sensorData);  // Utilisation de la méthode centralisée pour la sauvegarde
            return ResponseEntity.ok("NO_ACTION");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de la réception des données");
        }
    }


    @GetMapping("/config")
    public ResponseEntity<ConfigData> getConfig() {
        ConfigData configData = new ConfigData();
        configData.setModeAuto(modeAuto);
        configData.setSeuil(seuil);
        return ResponseEntity.ok(configData);
    }

    // Endpoint pour changer la configuration (mode et seuil)
    /*@PostMapping("/config")
    public ResponseEntity<String> updateConfig(@RequestBody ConfigData newConfig) {
        this.modeAuto = newConfig.isModeAuto();
        this.seuil = newConfig.getSeuil();

        // Sauvegarde de l'historique
        ConfigHistory history = new ConfigHistory(modeAuto, seuil, LocalDateTime.now());
        configHistoryRepository.save(history);

        return ResponseEntity.ok("Configuration updated and history saved");
    }*/

    @PostMapping("/config")
    public String setConfig(@RequestBody ConfigData configRequest) throws JsonProcessingException {
        boolean modeAuto = configRequest.isModeAuto();
        int seuil = configRequest.getSeuil();

        // Vous pouvez aussi ici envoyer ces informations au périphérique ESP8266
        // par exemple via HTTP en appelant un autre service qui enverra ces infos au capteur
        // Je vais simuler cette action ici.
        updateConfig(modeAuto, seuil);
        // Simuler l'envoi des données au périphérique ESP8266
        //System.out.println("Mode auto: " + modeAuto + ", Seuil: " + configRequest);

        return "Configuration mise à jour.";
    }

    // Nouvelle méthode GET pour récupérer toutes les données de capteurs
    @GetMapping
    public ResponseEntity<List<SensorDataEntity>> getAllSensorData() {
        List<SensorDataEntity> dataList = sensorDataRepository.findFirstByOrderByIdDesc();
        return ResponseEntity.ok(dataList);
    }

    // Endpoint pour récupérer l'historique des configurations
    @GetMapping("/config/history")
    public ResponseEntity<List<ConfigHistory>> getConfigHistory() {
        List<ConfigHistory> history = configHistoryRepository.findAll();
        return ResponseEntity.ok(history);
    }

    // Méthode pour sauvegarder les données des capteurs
    private void saveSensorData(SensorData sensorData) {
        SensorDataEntity entity = new SensorDataEntity();
        entity.setSoilMoisture(sensorData.getSoilMoisture());
        entity.setTemperature(sensorData.getTemperature());
        entity.setTimestamp(LocalDateTime.now());

        sensorDataRepository.save(entity);
    }

    // Méthode pour envoyer la configuration à l'ESP8266
    public void updateConfig(boolean modeAuto, int valSeuil) throws JsonProcessingException {
        String url = ESP8266_URL + "?modeAuto=" + modeAuto + "&seuil=" + valSeuil;
        try {
            String response = restTemplate.postForObject(url, null, String.class);
            var resp = mapper.readValue(response, Object.class);
            if (resp != null){
                System.out.println("response ::" +resp);
            }
        }catch (ResourceAccessException ex){
            throw new ApiException("Une erreur est survenue !") ;
        }
    }

    /*private boolean modeAuto = true;
    private int seuil = 20;


    private static final String ESP8266_URL = "http://192.168.244.10:8182/api/v1/sensor/config";

    private final RestTemplate restTemplate;

    private final AlertService alertService;
    private final SensorDataRepository sensorDataRepository;
    private final ConfigHistoryRepository configHistoryRepository;

    *//*@PostMapping
    public ResponseEntity<String> receiveSensorData(@RequestBody SensorData sensorData) {
        double soilMoisture = sensorData.getSoilMoisture();
        double temperature = sensorData.getTemperature();

        System.out.println("Données reçues - Humidité: " + soilMoisture + "%, Température: " + temperature + "°C");

        if (modeAuto && soilMoisture < seuil) {
            alertService.sendLowMoistureAlert();
            return ResponseEntity.ok("ACTIVATE_PUMP");
        }

        // Sauvegarde des données dans la base de données
        SensorDataEntity entity = new SensorDataEntity();
        entity.setSoilMoisture(soilMoisture);
        entity.setTemperature(sensorData.getTemperature());
        entity.setTimestamp(LocalDateTime.now());

        sensorDataRepository.save(entity);
        return ResponseEntity.ok("NO_ACTION");
    }
*//*

    @PostMapping
    public ResponseEntity<String> receiveSensorData(@RequestBody SensorData sensorData) {
        try {
            double soilMoisture = sensorData.getSoilMoisture();
            double temperature = sensorData.getTemperature();

            System.out.println("Données reçues - Humidité: " + soilMoisture + "%, Température: " + temperature + "°C");

            if (modeAuto && soilMoisture < seuil) {
                alertService.sendLowMoistureAlert();
                return ResponseEntity.ok("ACTIVATE_PUMP");
            }

            saveSensorData(sensorData);  // Utilisation de la méthode centralisée pour la sauvegarde
            return ResponseEntity.ok("NO_ACTION");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur lors de la réception des données");
        }
    }


    @GetMapping("/config")
    public ResponseEntity<ConfigData> getConfig() {
        ConfigData configData = new ConfigData();
        configData.setModeAuto(modeAuto);
        configData.setSeuil(seuil);
        return ResponseEntity.ok(configData);
    }

    // Endpoint pour changer la configuration (mode et seuil)
    *//*@PostMapping("/config")
    public ResponseEntity<String> updateConfig(@RequestBody ConfigData newConfig) {
        this.modeAuto = newConfig.isModeAuto();
        this.seuil = newConfig.getSeuil();

        // Sauvegarde de l'historique
        ConfigHistory history = new ConfigHistory(modeAuto, seuil, LocalDateTime.now());
        configHistoryRepository.save(history);

        return ResponseEntity.ok("Configuration updated and history saved");
    }*//*

    @PostMapping("/config")
    public String setConfig(@RequestBody ConfigData configRequest) {
        this.modeAuto = configRequest.isModeAuto();
        this.seuil = configRequest.getSeuil();
        this.
        // Vous pouvez aussi ici envoyer ces informations au périphérique ESP8266
        // par exemple via HTTP en appelant un autre service qui enverra ces infos au capteur
        // Je vais simuler cette action ici.
        updateConfig(this.modeAuto, this.seuil);
        // Simuler l'envoi des données au périphérique ESP8266
        //System.out.println("Mode auto: " + modeAuto + ", Seuil: " + configRequest);

        return "Configuration mise à jour.";
    }

    // Nouvelle méthode GET pour récupérer toutes les données de capteurs
    @GetMapping
    public ResponseEntity<List<SensorDataEntity>> getAllSensorData() {
        List<SensorDataEntity> dataList = sensorDataRepository.findFirstByOrderByIdDesc();
        return ResponseEntity.ok(dataList);
    }

    // Endpoint pour récupérer l'historique des configurations
    @GetMapping("/config/history")
    public ResponseEntity<List<ConfigHistory>> getConfigHistory() {
        List<ConfigHistory> history = configHistoryRepository.findAll();
        return ResponseEntity.ok(history);
    }

    // Méthode pour sauvegarder les données des capteurs
    private void saveSensorData(SensorData sensorData) {
        SensorDataEntity entity = new SensorDataEntity();
        entity.setSoilMoisture(sensorData.getSoilMoisture());
        entity.setTemperature(sensorData.getTemperature());
        entity.setTimestamp(LocalDateTime.now());

        sensorDataRepository.save(entity);
    }

    // Méthode pour envoyer la configuration à l'ESP8266
    public void updateConfig(boolean modeAuto, int seuil) {
        String url = ESP8266_URL + "?modeAuto=" + modeAuto + "&seuil=" + seuil;
        restTemplate.postForObject(url, null, String.class);
    }*/
}
