package com.team.dev.controller;

import com.team.dev.dto.*;
import com.team.dev.exception.ApiException;
import com.team.dev.model.Role;
import com.team.dev.model.User;
import com.team.dev.service.AuthService;
import com.team.dev.service.FileService;
import com.team.dev.utils.AppConstants;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class ApiController {

    @Value("${project.image}")
    private String path;
    private final FileService fileService;
    private final AuthService service;
    @GetMapping
    //@RolesAllowed({"ROLE_ADMIN"})
    public ResponseEntity<PageResponse> getAllUsers(
            @RequestParam(value = "keywords", defaultValue = AppConstants.DEFAULT_KEY_WORDS, required = false) String keywords,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        if (pageNo < 0){
            throw  new ApiException("L'index de la page ne doit être positif !");
        }
        PageResponse list = this.service.getAllUsers(pageNo, pageSize, sortBy, sortDir, keywords);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    @GetMapping("/admin")
    @RolesAllowed({"SUPER_ADMIN"})
    public ResponseEntity<PageResponse> getAllUser(
            @RequestParam(value = "keywords", defaultValue = AppConstants.DEFAULT_KEY_WORDS, required = false) String keywords,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        if (pageNo < 0){
            throw  new ApiException("L'index de la page ne doit être positif !");
        }
        PageResponse list = this.service.getAllUser(pageNo, pageSize, sortBy, sortDir, keywords);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthenticateResponse> getUser(@PathVariable Long id){
        //AuthenticateResponse user = this.service.getUserById(id);
        AuthenticateResponse authenticateResponse = this.service.getUserById(id);
        return new ResponseEntity<>(authenticateResponse, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ApiResponse registerEntrepriseUsers(
            @RequestBody @Valid RegisterRequest request
    ) {
        try {
            AuthenticateResponse response = service.registerEntrepriseUsers(request);
            return new ApiResponse(Boolean.TRUE, "Utilisateur ajouter avec succès !");
        }catch (MailException e){
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR,"Une erreur est survenue ! Veuillez Réessayer !");
        }
	/*{
		try {
			AuthenticateResponse response = service.register(request, servletRequest);
			return new ApiResponse(Boolean.TRUE, "Success ! Please, check your email to complete your registration");
		}catch (HttpServerErrorException.InternalServerError e){
			throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Veuillez réessayer utérieurement !");
		}*/

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> EditUser(@Valid @RequestBody AuthenticateResponse response, @PathVariable(value = "id") Long id){
        //AuthenticateResponse user = this.service.getUserById(id);
        AuthenticateResponse updateAuth = this.service.updateUser(response, id);
        return ResponseEntity.ok(new ApiResponse(Boolean.TRUE, "Edité avec succès !"));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> EditStatusUser(@PathVariable Long id){
        //AuthenticateResponse user = this.service.getUserById(id);
        AuthenticateResponse updateAuth = this.service.updateStatusUser( id);
        return new ResponseEntity<>(updateAuth, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ApiException deleteUser( @PathVariable Long id){
        //AuthenticateResponse user = this.service.getUserById(id);
        this.service.deleteUser(id);
        return  new ApiException(HttpStatus.OK, "L'Utilisateur supprimé avec succès !");
    }

    @PostMapping("/image/{id}")
    public ResponseEntity<AuthenticateResponse> uploadProductImage(
            @RequestParam("image") MultipartFile image,
            @PathVariable Long id
    ) throws Exception {
        String fileName = this.fileService.uploadImage(path, image);
        AuthenticateResponse auth = this.service.getUserById(id);
        auth.setImageUrl(fileName);
        AuthenticateResponse updateProduct = this.service.updateUser(auth, id);
        return new ResponseEntity<>(updateProduct, HttpStatus.OK);
    }

    //Get image by name
    @GetMapping(value = "/image/{imageName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(
            @PathVariable("imageName") String imageName, HttpServletResponse response
    ) throws IOException {

        InputStream resource = this.fileService.getRessource(path, imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        //response.encodeRedirectURL(resource.toString());
        StreamUtils.copy(resource, response.getOutputStream());
    }

    @PutMapping("/add-role-user")
    //@RolesAllowed({"ROLE_ADMIN"})
    public ResponseEntity<ApiResponse> addRoleToUser(@RequestBody @Valid RoleForm roleForm){
        service.addRoleToUser(roleForm);
        return ResponseEntity.ok( new ApiResponse("Role ajouté avec succès !"));
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRoles(){
        List<Role> role = service.getRole();

        return ResponseEntity.ok(role);
    }
    @GetMapping("/profile")
    public User user(Principal principal){
        return service.getUserByEmail(principal.getName());
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<?> changerPassword(
            @RequestBody @Valid ChangerPaswword request,
            Principal connectedUser
    ) {
        try {
            AuthenticateResponse response = service.changerPassword(request, connectedUser);
            return ResponseEntity.ok().build();
            //return new ApiResponse(Boolean.TRUE, "Success ! Password changed");
        }catch (HttpServerErrorException.InternalServerError e){
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Veuillez réessayer utérieurement !");
        }

    }

    @PostMapping("/{roleId}/{userId}")
    //@RolesAllowed({"ROLE_ADMIN"})
    public  ResponseEntity<AuthenticateResponse> removeRoleFromUser(@PathVariable final Long roleId, @PathVariable final Long userId){
        AuthenticateResponse response = service.removeCategoryFromProduct(roleId, userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
