package pro.sky.telegrambotteamwork.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambotteamwork.model.Dog;
import pro.sky.telegrambotteamwork.service.DogService;

@RestController
@RequestMapping("/api/dog")
@AllArgsConstructor
public class DogController {
    private final DogService dogService;

    @PostMapping
    public Dog addDog(@RequestBody Dog dog) {
        return dogService.addDog(dog);
    }

    @PutMapping
    public Dog updateDog(@RequestBody Dog dog) {
        return dogService.updateDog(dog);
    }

    @GetMapping("/{id}")
    public Dog findDog(@PathVariable Long id) {
        return dogService.findDog(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Dog> deleteDog(@PathVariable Long id) {
        dogService.deleteDog(id);
        return ResponseEntity.ok().build();
    }
}
