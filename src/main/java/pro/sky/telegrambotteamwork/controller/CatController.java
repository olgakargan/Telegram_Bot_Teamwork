package pro.sky.telegrambotteamwork.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambotteamwork.model.Cat;
import pro.sky.telegrambotteamwork.service.CatService;

/**
 * Класс-контроллер для работы с питомцем - кошкой
 */
@RestController
@RequestMapping("/api/cat")
@AllArgsConstructor
@Tag(name = "Работа с кошкой", description = "Позволяет управлять методами по работе с кошкой")
public class CatController {
    private final CatService catService;

    /**
     * Метод добавления кошки в базу данных
     *
     * @param cat сущность кошки
     * @return Возвращает сохраненную в базу данных кошку
     */
    @Operation(summary = "Метод добавления кошки в базу данных", description = "Позваляет добавлять кошку в базу данных")
    @PostMapping
    public Cat addCat(@RequestBody Cat cat) {
        return catService.addCat(cat);
    }

    /**
     * Метод редактирования кошки в базе данных
     *
     * @param cat сущность кошки
     * @return Возвращает отредактированную в базе данных кошку
     */
    @Operation(summary = "Метод редактирования кошки в базе данных", description = "Позваляет редактировать кошку в базе данных")
    @PutMapping
    public Cat updateCat(@RequestBody Cat cat) {
        return catService.updateCat(cat);
    }

    /**
     * Метод поиска кошки в базе данных
     *
     * @param id идентификатор искомой кошки
     * @return Возвращает найденную кошку
     */
    @Operation(summary = "Метод, чтобы найти кошку в базе данных", description = "Позваляет найти кошку в базе данных")
    @GetMapping("/{id}")
    public Cat findCat(@Parameter(description = "Идентификатор искомой кошки") @PathVariable Long id) {
        return catService.findCat(id);
    }

    /**
     * Метод удаления кошки из базы данных
     *
     * @param id идентификатор кошки
     * @return Возвращает ответ 200, если удаление кошки успешно произошло
     */
    @Operation(summary = "Метод, чтобы удалить кошку из базы данных", description = "Позваляет удалить кошку из базы данных")
    @DeleteMapping("/{id}")
    public ResponseEntity<Cat> deleteCat(@Parameter(description = "Идентификатор удаляемой кошки") @PathVariable Long id) {
        catService.deleteCat(id);
        return ResponseEntity.ok().build();
    }
}
