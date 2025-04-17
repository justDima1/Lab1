package model.buildings;

import model.heroes.Hero;
import model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tavern extends Building{ // Tavern теперь подкласс Building

    private List<Hero> availableHeroes;
    private Random random = new Random();

    public Tavern() {
        super("таверна", 500); // Вызываем конструктор Building
        this.availableHeroes = generateHeroes(); // Генерируем героев при создании таверны
    }

    public List<Hero> getAvailableHeroes() {
        return availableHeroes;
    }

    public Hero buyHero(int heroIndex, Player player) { //принимает индекс выбранного героя и игрока, который его покупает
        if (heroIndex >= 0 && heroIndex < availableHeroes.size()) {
            Hero hero = availableHeroes.get(heroIndex);
            if (player.getGold().getAmount() >= 500) { // Стоимость героя. ИСПРАВЛЕННАЯ СТРОКА
                player.getGold().setAmount(player.getGold().getAmount() - 500); // Вычитаем золото
                availableHeroes.remove(hero); // Убираем героя из таверны
                return hero;
            } else {
                System.out.println("Недостаточно золота для покупки этого героя.");
                return null;
            }
        } else {
            System.out.println("Неверный номер героя.");
            return null;
        }
    }

    private List<Hero> generateHeroes() {
        List<Hero> heroes = new ArrayList<>();
        // Создаем несколько героев с разными характеристиками
        heroes.add(new Hero(1, 1, "Соня"));
        heroes.add(new Hero(1, 1, "Вика"));
        return heroes;
    }
}