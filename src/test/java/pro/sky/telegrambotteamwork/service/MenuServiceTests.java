package pro.sky.telegrambotteamwork.service;

import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MenuServiceTests {

    private MenuService menuService;
    private Update update;

    public static final String TEST_MESSAGE = "Test text message";
    public static final String TEST_BUTTON_1 = "Test button 1";
    public static final String TEST_BUTTON_2 = "Test button 2";
    public static final String TEST_BUTTON_3 = "Test button 3";
    public static final String TEST_BUTTON_4 = "Test button 4";
    public static final String TEST_BUTTON_5 = "Test button 5";

    public static final List<String> TEST_MENU = List.of(
            TEST_BUTTON_1,
            TEST_BUTTON_2,
            TEST_BUTTON_3,
            TEST_BUTTON_4,
            TEST_BUTTON_5
    );

    @Test
    public void loadingTheMenuTest() {
        assertEquals(TEST_BUTTON_1, TEST_MENU.get(0));
        assertThrows(RuntimeException.class, () -> menuService.loadingTheMenu(update.message(), TEST_MESSAGE, TEST_MENU));
    }

    @Test
    public void loadingTheMenuCallbackQueryTest() {
        assertEquals(TEST_BUTTON_2, TEST_MENU.get(1));
        assertThrows(RuntimeException.class, () -> menuService.loadingTheMenuCallbackQuery(update, TEST_MESSAGE, TEST_MENU));
    }

    @Test
    public void loadingTheMenuDogAndCatTest() {
        assertEquals(TEST_BUTTON_3, TEST_MENU.get(2));
        assertThrows(RuntimeException.class, () -> menuService.loadingTheMenuDogAndCat(update, TEST_MESSAGE, TEST_MENU));
    }

    @Test
    public void loadingTheMenuDogAndCatCallbackQueryTest() {
        assertEquals(TEST_BUTTON_4, TEST_MENU.get(3));
        assertThrows(RuntimeException.class, () -> menuService.loadingTheMenuDogAndCatCallbackQuery(update, TEST_MESSAGE, TEST_MENU));
    }

    @Test
    public void loadingTheMenuSubscribeTest() {
        assertEquals(TEST_BUTTON_5, TEST_MENU.get(4));
        assertThrows(RuntimeException.class, () -> menuService.loadingTheMenuSubscribe(update, TEST_MESSAGE));
    }

}
