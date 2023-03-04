package pro.sky.telegrambotteamwork.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pro.sky.telegrambotteamwork.repository.UiRepository;
import pro.sky.telegrambotteamwork.service.UiService;

@RestController
@RequestMapping("volunteer")
public class UiController {

    private final UiService uiService;
    private final UiRepository uiRepository;

    public UiController(UiService uiService,
                        UiRepository uiRepository) {
        this.uiService = uiService;
        this.uiRepository = uiRepository;
    }
}

