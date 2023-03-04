package pro.sky.telegrambotteamwork.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambotteamwork.model.User;
import pro.sky.telegrambotteamwork.service.UserService;

@RestController
@RequestMapping("user")

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("{id}")
    public User getUserInfo(@PathVariable Long id) {
        return userService.readInfo(id);
    }

    @PostMapping
    public HttpStatus registerNewUser(@RequestBody User user) {
        userService.addUser(user);
        return HttpStatus.OK;
    }

    @DeleteMapping
    public HttpStatus deleteUser(@RequestBody User user) {
        userService.deleteUser(user);
        return HttpStatus.OK;
    }
}