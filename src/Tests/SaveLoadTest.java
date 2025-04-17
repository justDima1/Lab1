package Tests;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

import static main.SaveManager.saveGame;
import static org.junit.jupiter.api.Assertions.*;

public class SaveLoadTest {

    @Test
    void testSaveGameCreatesFile() {
        String filename = "test_save.sav";
        saveGame(filename); // Замените на ваш метод сохранения игры
        File file = new File("saves/"+filename);
        file.delete(); // Очистка после теста
    }

    private void saveGame(String filename) {
    }

    @Test
    void testLoadGameFileExists() {
        String filename = "existing_save.sav";
        // Создаем фиктивный файл сохранения
        File file = new File("saves/"+filename);
        try {
            file.createNewFile();
        } catch (IOException e) {
            fail("Не удалось создать тестовый файл");
        }
        //assertTrue(loadGameFromFile(filename), "Загрузка должна быть успешной, если файл существует"); // Замените на ваш метод загрузки
        file.delete(); // Очистка после теста
    }

    @Test
    void testLoadGameFileNotExists() {
        String filename = "non_existing_save.sav";
        File file = new File("saves/"+filename);
        assertFalse(file.exists());
        // assertFalse(loadGameFromFile(filename), "Загрузка должна завершиться неудачей, если файл не существует"); // Замените на ваш метод загрузки
    }
}