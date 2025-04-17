package model;

import java.util.ArrayList;
import java.util.List;

import model.buildings.*;
import org.junit.jupiter.api.Test;

public class City {
    private int x;
    private int y;
    private String name;
    private List<Building> buildings;

    public City(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.buildings = new ArrayList<>();
        this.buildings.add(new TownHall());
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public List<Building> getBuildings() {
        return buildings;
    }

    public void addBuilding(Building building) {
        this.buildings.add(building);
    }

    public int getGoldIncome() {
        int income = 0;
        for (Building building : buildings) {
            if (building instanceof TownHall) {
                TownHall townHall = (TownHall) building;
                income = townHall.getGoldIncome(); // Получаем доход от ратуши
            }
        }
        return income;
    }

    public TownHall getTownHall() {
        for (Building building : buildings) {
            if (building instanceof TownHall) {
                return (TownHall) building;
            }
        }
        return null;
    }
    public boolean hasTownHall() {
        return getTownHall() != null;
    }
    public List<RecruitBuilding> getRecruitBuildings() {
        List<RecruitBuilding> recruitBuildings = new ArrayList<>();
        for (Building building : buildings) {
            if (building instanceof RecruitBuilding) {
                recruitBuildings.add((RecruitBuilding) building);
            }
        }
        return recruitBuildings;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}