package pro.sky.telegrambotteamwork.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambotteamwork.model.User;
import pro.sky.telegrambotteamwork.service.UserService;

/**
 * Класс-контроллер для работы со всеми пользователями
 */
@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
@Tag(name = "Работа с пользователями", description = "Позволяет управлять методами по работе со всеми пользователями")
public class UserController {
    private final UserService userService;

    /**
     * Метод, добавляющий пользователя в базу данных
     *
     * @param user сущность пользователя ботом
     * @return Возвращает сохраненного пользователя
     */
    @Operation(summary = "Метод добавления пользователя в базу данных", description = "Позваляет добавлять пользователя в базу данных")
    @PostMapping
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    /**
     * Метод, который редактируект пользователя
     *
     * @param user сущность пользователя ботом
     * @return Возвращает измененного пользователя
     */
    @Operation(summary = "Метод редактирования пользователя в базе данных", description = "Позваляет редактировать пользователя в базе данных")
    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    /**
     * Метод поиска пользователя в базе данных
     *
     * @param id идентификатор искомого пользователя
     * @return Возвращает найденного пользователя
     */
    @Operation(summary = "Метод, чтобы найти пользователя в базе данных", description = "Позваляет найти пользователя в базе данных")
    @GetMapping("/{id}")
    public User findUser(@Parameter(description = "Идентификатор искомого пользователя") @PathVariable Long id) {
        return userService.findUser(id);
    }

}
