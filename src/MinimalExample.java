public class MinimalExample {

    public static void main(String[] args) {
        int width = 10;
        int height = 10;
        String[][] map = new String[width][height];

        // Заполняем карту травой
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                map[i][j] = ".";
            }
        }

        // Размещаем замки
        map[0][0] = "C";
        map[width - 1][height - 1] = "E";

        // Размещаем героя
        int heroX = 1;
        int heroY = 1;

        // Отображаем карту
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (i == heroX && j == heroY) {
                    System.out.print("H ");
                } else {
                    System.out.print(map[i][j] + " ");
                }
            }
            System.out.println();
        }
    }
}
