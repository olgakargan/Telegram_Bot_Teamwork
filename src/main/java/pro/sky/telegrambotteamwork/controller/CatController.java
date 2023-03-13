import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.sky.telegrambotteamwork.model.Cat;
import pro.sky.telegrambotteamwork.service.CatService;

@RestController
@RequestMapping("/api/cat")
@AllArgsConstructor
public class CatController {
    private final CatService catService;

    @PostMapping
    public Cat addCat(@RequestBody Cat cat) {
        return catService.addCat(cat);
    }

    @PutMapping
    public Cat updateCat(@RequestBody Cat cat) {
        return catService.updateCat(cat);
    }

    @GetMapping("/{id}")
    public Cat findCat(@PathVariable Long id) {
        return catService.findCat(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Cat> deleteCat(@PathVariable Long id) {
        catService.deleteCat(id);
        return ResponseEntity.ok().build();
    }
}