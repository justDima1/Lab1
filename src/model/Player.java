package model;

import model.heroes.Hero;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private List<Hero> heroes = new ArrayList<>(); // Список героев
    private Hero currentHero; // Текущий герой
    private int gold;
    private String gems;

    public Player(String name, Hero hero, int startGold) { // Изменяем конструктор
        this.name = name;
        this.heroes.add(hero);
        this.currentHero = hero;
        this.gold = startGold; //Используем переданное значение
    }

    public String getName() {
        return name;
    }

    public List<Hero> getHeroes() {
        return heroes;
    }

    public Hero getCurrentHero() { // Получить текущего героя
        return currentHero;
    }

    public void setCurrentHero(Hero currentHero) { // Установить текущего героя
        this.currentHero = currentHero;
    }

    public int getGold() { // ДОБАВЛЯЕМ
        return gold;
    }

    public void setGold(int gold) { // ДОБАВЛЯЕМ
        this.gold = gold;
    }

    public String getGems() {
        return gems;
    }

    public void setGems(String gems) {
        this.gems = gems;
    }
}