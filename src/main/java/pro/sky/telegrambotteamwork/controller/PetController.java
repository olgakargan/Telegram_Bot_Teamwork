package pro.sky.telegrambotteamwork.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambotteamwork.model.Pet;
import pro.sky.telegrambotteamwork.service.PetService;


@RestController
@RequestMapping("/api/pet")
@Tag(name = "Работа с животными", description = "Позволяет управлять населением питомника")
@AllArgsConstructor
public class PetController {
    private final PetService petService;

    @Operation(
            summary = "Приём животного в приют",
            description = "Позволяет зарегистрировать новое животное в системе")
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Pet.class))
    )
    @PostMapping
    public Pet addPet(@RequestBody Pet pet) {
        return petService.addPet(pet);
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
    @PutMapping
    public Pet updatePet(@RequestBody Pet pet) {
        return petService.updatePet(pet);
    }

    @GetMapping("/{id}")
    public Pet findPet(@PathVariable Long id) {
        return petService.findPet(id);
    }
}