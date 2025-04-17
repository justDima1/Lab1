package model.map;

import java.util.Scanner;

public class MapEditor {
    private char[][] map;
    private int width;
    private int height;
    private Scanner scanner;
    public MapEditor(int width, int height) {
        this.width = width;
        this.height = height;
        this.map = new char[height][width];
        this.scanner = new Scanner(System.in);
        initializeMap();
    }
    private void initializeMap() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                map[i][j] = '.'; // Изначально вся карта - земля
            }
        }
    }

    public void startEditor() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            displayMap();
            System.out.println("Введите команду (e - редактировать, s - сохранить, l - загрузить, q - выйти):");
            String command = scanner.nextLine();
            switch (command) {
                case "e":
                    editMap();
                    break;
                case "s":
                    saveMap();
                    break;
                case "l":
                    loadMap();
                    break;
                case "q":
                    System.out.println("Выход из редактора карт.");
                    return;
                default:
                    System.out.println("Неизвестная команда.");
            }
        }
    }
    public void loadMap() {
        System.out.println("Введите имя файла для загрузки карты:");
        String filename = scanner.nextLine();
        try (Scanner fileScanner = new Scanner(new java.io.File(filename + ".txt"))) {
            for (int i = 0; i < height; i++) {
                if (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    for (int j = 0; j < width; j++) {
                        if (j < line.length()) {
                            map[i][j] = line.charAt(j);
                        } else {
                            map[i][j] = '.';
                        }
                    }
                } else {
                    for (int j = 0; j < width; j++) {
                        map[i][j] = '.';
                    }
                }
            }
            System.out.println("Карта загружена из файла " + filename + ".txt");
        } catch (java.io.FileNotFoundException e) {
            System.out.println("Файл не найден.");
        }
    }
    private void displayMap() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }
    public void editMap(int x, int y, char symbol) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            if (isValidSymbol(symbol)) {
                map[y][x] = symbol;
            } else {
                System.out.println("Недопустимый символ.");
            }
        } else {
            System.out.println("Некорректные координаты.");
        }
    }
    public void editMap() {
        try {
            System.out.println("Введите координаты x и y (от 0 до " + (width - 1) + " и " + (height - 1) + "):");
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            scanner.nextLine();
            if (x >= 0 && x < width && y >= 0 && y < height) {
                System.out.println("Введите символ для размещения:");
                String symbolStr = scanner.nextLine();
                if (symbolStr.length() == 1) {
                    char symbol = symbolStr.charAt(0);
                    if (isValidSymbol(symbol)) {
                        map[y][x] = symbol;
                    } else {
                        System.out.println("Недопустимый символ.");
                    }
                } else {
                    System.out.println("Необходимо ввести один символ.");
                }
            } else {
                System.out.println("Некорректные координаты.");
            }
        } catch (java.util.InputMismatchException e) {
            System.out.println("Ошибка: Введите числовые координаты.");
            scanner.nextLine(); // Очищаем буфер сканера
        }
    }

    private boolean isValidSymbol(char symbol) {
        return symbol == '.' || symbol == 'T' || symbol == 'G' || symbol == 'Z' || symbol == 'H' || symbol == 'C' || symbol == '+'|| symbol == 'X'|| symbol == 'E'|| symbol == 'A';
    }
    private void saveMap() {
        System.out.println("Введите имя файла для сохранения карты:");
        String filename = scanner.nextLine();
        try (java.io.PrintWriter writer = new java.io.PrintWriter(filename + ".txt")) {
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    writer.print(map[i][j]);
                }
                writer.println();
            }
            System.out.println("Карта сохранена в файл " + filename + ".txt");
        } catch (java.io.FileNotFoundException e) {
            System.out.println("Ошибка сохранения");
        }
    }
    public char[][] getMap() {
        return map;
    }
}