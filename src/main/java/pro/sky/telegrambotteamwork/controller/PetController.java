package pro.sky.telegrambotteamwork.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambotteamwork.model.Pet;
import pro.sky.telegrambotteamwork.service.PetService;

import java.util.Collection;
import java.util.Collections;

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
            description = "добавлено новое животное",
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
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "параметры животного изменены",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Pet.class))
            )
    })
    @PostMapping("/{petId}")
    public HttpStatus updatePet(@PathVariable("petId") Long petId, @RequestBody Pet pet) {
        petService.updatePet(petId, pet);
        return HttpStatus.OK;
    }

    @Operation(
            summary = "Поиск животного",
            description = "Позволяет найти животного по его id")
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Pet.class))
    )
    @GetMapping("/{petId}")
    public ResponseEntity<Pet> getPet(@PathVariable("petId") Long petId) {
        return new ResponseEntity<>(petService.findPetById(petId), HttpStatus.OK);
    }

    @Operation(
            summary = "Все животные в приюте",
            description = "Выводит список всех животных")
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Pet.class))
    )
    @GetMapping("/all")
    public ResponseEntity<Collection<Pet>> getAllPets() {
        return new ResponseEntity<>(Collections.unmodifiableCollection(petService.findAll()), HttpStatus.OK);
    }

    @Operation(
            summary = "Удаление записи о животном",
            description = "Удаляет запись о животном из БД")
    @ApiResponse(
            responseCode = "200",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = Pet.class))
    )
    @DeleteMapping("/{petId}")
    public ResponseEntity<Pet> deleteTodo(@PathVariable("petId") Long petId) {
        petService.deletePet(petId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}