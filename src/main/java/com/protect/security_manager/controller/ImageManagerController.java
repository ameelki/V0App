package com.protect.security_manager.controller;
import com.protect.security_manager.service.ImageService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;
import security.manager.api.ImageManagerApiController;
import security.manager.model.ImageManagerGetPictureImageTypeGet200Response;
import security.manager.model.ImageManagerUploadImageTypePostRequest;
import java.io.IOException;
import java.util.Optional;
@Controller
public class ImageManagerController extends ImageManagerApiController {

    @Autowired
    private ImageService imageService;


    public ImageManagerController(NativeWebRequest request) {
        super(request);
    }



   public ResponseEntity<String> imageManagerUploadImageTypePost(

            @PathVariable("imageType") String imageType,

            @Valid @RequestBody ImageManagerUploadImageTypePostRequest imageManagerUploadImageTypePostRequest
    ) {
        try {
            // Récupère les données de la requête
            String base64Image = imageManagerUploadImageTypePostRequest.getImage();
            Optional<String> personId = Optional.ofNullable(imageManagerUploadImageTypePostRequest.getPersonId());
            Optional<Integer> addsId = Optional.ofNullable(imageManagerUploadImageTypePostRequest.getAddsId());

            // Appelle la méthode de sauvegarde de l'image
            String result = this.imageService.saveImage(base64Image, imageType, personId, addsId,imageManagerUploadImageTypePostRequest.getFileName());

            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'enregistrement de l'image");
        }
   }
    @PreAuthorize("@authorizationService.hasRoleAndCheckResourcesForSameConnectedPerson(#authorization, 'client',#personId)")

    public ResponseEntity<ImageManagerGetPictureImageTypeGet200Response> imageManagerGetPictureImageTypeGet(
            @io.swagger.v3.oas.annotations.Parameter(name = "imageType", description = "Type de l'image à récupérer (PERSON ou ADDS)", required = true, in = ParameterIn.PATH) @PathVariable("imageType") String imageType,
            @NotNull @Parameter(name = "Authorization", description = "Token d'authentification Bearer pour valider l'accès", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "Authorization", required = true) String authorization,
            @io.swagger.v3.oas.annotations.Parameter(name = "personId", description = "ID de la personne (requis si `imageType` est `PERSON`)", in = ParameterIn.QUERY) @Valid @RequestParam(value = "personId", required = false) String personId,
            @Parameter(name = "addsId", description = "ID de l'ADDS (requis si `imageType` est `ADDS`)", in = ParameterIn.QUERY) @Valid @RequestParam(value = "addsId", required = false) Integer addsId
    ) {
        ImageManagerGetPictureImageTypeGet200Response imageManagerGetPictureImageTypeGet200Response=null;
        if ("PERSON".equalsIgnoreCase(imageType)) {
            if (personId == null) {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(imageManagerGetPictureImageTypeGet200Response);
            }
            imageManagerGetPictureImageTypeGet200Response = imageService.getImageBase64ByPersonId(personId);
            }

        return ResponseEntity.status(HttpStatus.OK).body(imageManagerGetPictureImageTypeGet200Response);

    }


}








