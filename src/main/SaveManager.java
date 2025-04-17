package main;

import java.io.*;

public class SaveManager {

    private static final String SAVE_DIRECTORY = "saves/"; // Папка для сохранений

    // Метод для сохранения игры
    public static void saveGame(SaveData data, String playerName, String saveName) {
        try {
            // Создаем папку для сохранений, если ее нет
            File saveDir = new File(SAVE_DIRECTORY);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }

            // Создаем имя файла сохранения
            String fileName = SAVE_DIRECTORY + playerName + "_" + saveName + ".sav";
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);

            // Сериализуем объект SaveData и записываем в файл
            objectOut.writeObject(data);

            objectOut.close();
            fileOut.close();
            System.out.println("Игра сохранена в " + fileName);

        } catch (IOException e) {
            System.err.println("Ошибка при сохранении игры: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Метод для загрузки игры
    public static SaveData loadGame(String playerName, String saveName) {
        SaveData data = null;
        try {
            String fileName = SAVE_DIRECTORY + playerName + "_" + saveName + ".sav";
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);

            // Десериализуем объект SaveData из файла
            data = (SaveData) objectIn.readObject();

            objectIn.close();
            fileIn.close();
            System.out.println("Игра загружена из " + fileName);

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Ошибка при загрузке игры: " + e.getMessage());
            e.printStackTrace();
        }
        return data;
    }

    // Метод для получения списка сохранений игрока
    public static File[] getSaveFiles(String playerName) {
        File saveDir = new File(SAVE_DIRECTORY);
        if (!saveDir.exists()) {
            return new File[0]; // Возвращаем пустой массив, если папка не существует
        }

        // Фильтруем файлы, чтобы получить только сохранения текущего игрока
        File[] saveFiles = saveDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(playerName + "_") && name.endsWith(".sav");
            }
        });

        return saveFiles;
    }
}