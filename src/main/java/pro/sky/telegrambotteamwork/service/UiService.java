package pro.sky.telegrambotteamwork.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambotteamwork.repository.UiRepository;

@Service
public class UiService {

    private final Logger logger = LoggerFactory.getLogger(PetService.class);
    private final UiRepository uiRepository;

    public UiService(UiRepository uiRepository) {
        this.uiRepository = uiRepository;
    }
}
