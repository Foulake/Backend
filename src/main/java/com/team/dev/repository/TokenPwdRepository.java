package com.team.dev.repository;

import com.team.dev.model.VerificationPwdToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenPwdRepository extends JpaRepository<VerificationPwdToken, Long> {
    Optional<VerificationPwdToken> findByToken(String token);
}
