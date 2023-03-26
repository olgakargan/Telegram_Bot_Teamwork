package pro.sky.telegrambotteamwork.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pro.sky.telegrambotteamwork.service.ImageService;

import java.io.IOException;

/**
 * Класс-контроллер для работы с изображениями
 */
@RestController
@RequestMapping("/api/image")
@AllArgsConstructor
@Tag(name = "Работа с изображениями", description = "Позволяет управлять методами по работе с изображениями")
public class ImageController {
    private final ImageService imageService;

    /**
     * Метод загрузки фотографий для питомцев
     *
     * @param id        идентификатор питомца
     * @param imageFile Медиафайл фотографии
     * @return Возвращает ответ 200, если добавление изображения успешно произошло
     * @throws IOException общий класс исключений ввода-вывода
     */
    @Operation(summary = "Метод добавления изображения в базу данных", description = "Позваляет добавлять изображение в базу данных")
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