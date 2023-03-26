package pro.sky.telegrambotteamwork.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambotteamwork.model.Dog;
import pro.sky.telegrambotteamwork.service.DogService;

/**
 * Класс-контроллер для работы с питомцем - собакой
 */
@RestController
@RequestMapping("/api/dog")
@AllArgsConstructor
@Tag(name = "Работа с собакой", description = "Позволяет управлять методами по работе с собакой")
public class DogController {
    private final DogService dogService;

    /**
     * Метод добавления собаки в базу данных
     *
     * @param dog сущность собаки
     * @return Возвращает сохраненную в базу данных собаку
     */
    @Operation(summary = "Метод добавления собаки в базу данных", description = "Позваляет добавлять собаку в базу данных")
    @PostMapping
    public Dog addDog(@RequestBody Dog dog) {
        return dogService.addDog(dog);
    }

    /**
     * Метод редактирования собаки в базе данных
     *
     * @param dog сущность собаки
     * @return Возвращает отредактированную в базе данных собаку
     */
    @Operation(summary = "Метод редактирования собаки в базе данных", description = "Позваляет редактировать собаку в базе данных")
    @PutMapping
    public Dog updateDog(@RequestBody Dog dog) {
        return dogService.updateDog(dog);
    }

    /**
     * Метод поиска собаки в базе данных
     *
     * @param id идентификатор искомой собаки
     * @return Возвращает найденную собаку
     */
    @Operation(summary = "Метод, чтобы найти собаку в базе данных", description = "Позваляет найти собаку в базе данных")
    @GetMapping("/{id}")
    public Dog findDog(@Parameter(description = "Идентификатор искомой собаки") @PathVariable Long id) {
        return dogService.findDog(id);
    }

    /**
     * Метод удаления собаки из базы данных
     *
     * @param id идентификатор собаки
     * @return Возвращает ответ 200, если удаление собаки успешно произошло
     */
    @Operation(summary = "Метод, чтобы удалить собаку из базы данных", description = "Позваляет удалить собаку из базы данных")
    @DeleteMapping("/{id}")
    public ResponseEntity<Dog> deleteDog(@Parameter(description = "Идентификатор удаляемой собаки") @PathVariable Long id) {
        dogService.deleteDog(id);
        return ResponseEntity.ok().build();
    }
}