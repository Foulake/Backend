package com.team.dev.repository;

import com.team.dev.model.ConfigHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigHistoryRepository extends JpaRepository<ConfigHistory, Long> {
}
