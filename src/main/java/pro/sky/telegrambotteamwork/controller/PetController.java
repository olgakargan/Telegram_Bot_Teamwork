package pro.sky.telegrambotteamwork.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambotteamwork.model.Pet;
import pro.sky.telegrambotteamwork.service.PetService;


@RestController
@RequestMapping("api/pet")
@Tag(name = "Работа с животными", description = "Позволяет управлять населением питомника")
public class PetController {
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @Operation(
            summary = "Приём животного в приют",
            description = "Позволяет зарегистрировать новое животное в системе")
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Pet.class))
    )
    @PutMapping
    public HttpStatus registerPet(@RequestBody Pet pet) {
        petService.addNew(pet);
        return HttpStatus.OK;
    }

    @Operation(
            summary = "Изменение данных животного",
            description = "Позволяет изменить параметры животного")
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Pet.class))
    )
    @PostMapping
    public HttpStatus updatePet(@RequestBody Pet pet) {
        if (!petService.findAll().contains(pet)) {
            return HttpStatus.NOT_FOUND;
        }
        petService.updatePet(pet);
        return HttpStatus.OK;
    }
}