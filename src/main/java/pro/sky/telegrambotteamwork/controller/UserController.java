package pro.sky.telegrambotteamwork.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambotteamwork.model.User;
import pro.sky.telegrambotteamwork.service.UserService;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable Long id) {
        return userService.findUser(id);
    }

}
