package pro.sky.telegrambotteamwork.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.telegrambotteamwork.service.ImageService;

import java.io.IOException;

@RestController
@RequestMapping("/api/image")
@AllArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping(value = "/{id}/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(@PathVariable Long id, @RequestParam MultipartFile imageFile) throws IOException {
        int sizeFile = 1024 * 300 * 50;
        if (imageFile.getSize() > sizeFile) {
            return ResponseEntity.badRequest().body("Файл слишком большой! Загрузите фотографию меньшего размера.");
        }
        imageService.uploadImage(id, imageFile);
        return ResponseEntity.ok().build();
    }
}
