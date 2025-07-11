package com.team.dev.service.impl;

import ch.qos.logback.core.spi.ErrorCodes;
import com.team.dev.dto.ChampRequestDto;
import com.team.dev.dto.ChampResponseDto;
import com.team.dev.exception.ApiException;
import com.team.dev.exception.ResourceNotFoundException;
import com.team.dev.model.User;
import com.team.dev.repository.UserRepository;
import com.team.dev.service.AuthService;
import com.team.dev.service.ChampService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.*;

//@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class ChampServiceImplTest {

    User user = new User();

    @Autowired
    private ChampService champService;

    @Autowired
    private AuthService service;
    @Autowired
    private UserRepository repository;

    /*public User getAuthUser() {
        UserDetails details = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = details.getUsername();
        return repository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("Utilisateur non trouvé !")
        );
    }*/

    // Ajout de champ
    @Test
    @WithMockUser(username = "sali@gmail.com", roles = "ADMIN")
    void saveChamp() {
        //String current = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        User authenticatedUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Long idEntreprise = authenticatedUser.getEntreprise().getEntrepriseId();

        ChampRequestDto champRequestDto = ChampRequestDto.builder()
                .nomChamp("Carotte")
                .localiteId(1L)
                //.entrepriseId(1L)
                .build();
        ChampResponseDto addedChamp = this.champService.addChamp(champRequestDto);

        System.out.println(addedChamp);
        assertNotNull(addedChamp);
        assertNotNull(addedChamp.getId());
        assertEquals(champRequestDto.getNomChamp(), addedChamp.getNomChamp());
        assertEquals(champRequestDto.getLocaliteId(), addedChamp.getLocaliteId());
        assertEquals(champRequestDto.getEntrepriseId(), addedChamp.getEntrepriseId());
    }

    @Test
    @WithMockUser(username = "sali@gmail.com", roles = "ADMIN")
    void ModifierChamp() {
            ChampRequestDto champRequestDto = ChampRequestDto.builder()
                    .nomChamp("Mais")
                    .localiteId(1L)
                    .build();
            ChampResponseDto addedChamp = this.champService.addChamp(champRequestDto);

            ChampResponseDto champEdit = addedChamp;
            champEdit.setNomChamp("Tomate");
            champEdit.setLocaliteId(1L);

            addedChamp = champService.addChamp(mapTo(champEdit));

            assertNotNull(champEdit);
            assertNotNull(champEdit.getId());
            assertEquals(champEdit.getNomChamp(), addedChamp.getNomChamp());
            assertEquals(champEdit.getLocaliteId(), addedChamp.getLocaliteId());
            assertEquals(champEdit.getEntrepriseId(), addedChamp.getEntrepriseId());
        }
    //}

    @Test
    @WithMockUser(username = "sali@gmail.com", roles = "ADMIN")
    void invalidChampException() {
        RuntimeException apiException = assertThrows(RuntimeException.class, () -> champService.getChampId(0L));

        assertEquals("Il n'existe pas de champ avec id : 0", apiException.getMessage());
    }


    private ChampRequestDto mapTo(ChampResponseDto response){
        ChampRequestDto requestDto = new ChampRequestDto();

        requestDto.setId(response.getId());
        requestDto.setNomChamp(response.getNomChamp());
        requestDto.setLocaliteId(response.getLocaliteId());
        requestDto.setNomLocalite(response.getNomLocalite());
        requestDto.setEntrepriseId(response.getEntrepriseId());
        requestDto.setImageUrl(response.getImageUrl());
        requestDto.setDownloadUrl(response.getDownloadUrl());
        return requestDto;
    }

}