package com.team.dev.repository;

import com.team.dev.model.Role;
import com.team.dev.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserRepository extends  JpaRepository<User, Long>{
	 Optional<User> findByEmail(String email);

    Optional<User> findUserByRolesAndId(Role roles , Long id);

    @Query("select u from User u where u.prenom like :key and u.entreprise.entrepriseId=:entrepriseId")
    Page<User> findByPrenomContainingAndEntreprise_Users(Pageable pageable, @Param("key") String prenom, Long entrepriseId);

    @Query("select u from User u where u.prenom like :key or u.nom like :key")
    Page<User> findByPrenomContainingOrNomContaining(Pageable p, @Param("key") String s);

    @Transactional
    @Modifying
    @Query("update User u set u.password = ?2 where u.email = ?1")
    void updatePassword(String email, String password);
}
