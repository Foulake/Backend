package com.team.dev.service.impl;

import com.team.dev.dto.*;
import com.team.dev.model.Entreprise;
import com.team.dev.service.AuthService;
import com.team.dev.service.EntrepriseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class EntrepriseUserService {

    private final EntrepriseService entrepriseService;
    private final AuthService userService;


    public EntrepriseUserService(EntrepriseService entrepriseService, AuthService userService) {
        this.entrepriseService = entrepriseService;
        this.userService = userService;
    }

    @Transactional
    public void enregistrerEntrepriseAvecUtilisateur(EntrepriseUserDto request, final HttpServletRequest servletRequest) {
        //try {
        log.info("Data request ::: {} ", request);
            EntrepriseResponseDto entrepriseSaved = this.entrepriseService.addEntreprise(request.getEntrepriseDto());
            Entreprise entreprise = this.entrepriseService.getEntreprise(entrepriseSaved.getIdEntreprise());
            // Associer l'utilisateur à l'entreprise
            request.getRequest().setEntreprise(entreprise);
            AuthenticateResponse userSaved= this.userService.register(request.getRequest(), servletRequest);

        /*} catch (Exception e) {
            throw new RuntimeException("Échec de l'enregistrement de l'entreprise ou de l'utilisateur", e);
        }*/
    }
}

