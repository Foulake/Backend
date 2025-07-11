package com.team.dev.repository;

import com.team.dev.model.SensorDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorDataRepository  extends JpaRepository<SensorDataEntity, Long> {
    //List<SensorDataEntity> findFirstByOrderByIdDesc();
    List<SensorDataEntity> findFirstByOrderByIdDesc();
}
