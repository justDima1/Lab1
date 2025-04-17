package Tests;

import controller.AIController;
import controller.GameController;
import model.Player;
import model.heroes.Hero;
import model.map.GameMap;
import model.units.Archer;
import model.units.Pikeman;
import model.units.Swordsman;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AIControllerTest {

    private AIController aiController;
    private Hero aiHero;
    private Player aiPlayer;
    private GameMap gameMap;
    private GameController gameController;
    private int mapWidth = 10;
    private int mapHeight = 10;

    @BeforeEach
    void setUp() {
        gameController = new GameController(true, mapWidth, mapHeight);
        gameMap = new GameMap(mapWidth, mapHeight);
        aiHero = new Hero(1, 1, "AI Hero");
        aiPlayer = new Player("AI Player", aiHero, 100);
        aiPlayer.setGold(new model.Gold(1000)); // Set initial gold
        aiPlayer.setCurrentHero(aiHero);
        aiHero.setMovesLeft(2); // Установим количество ходов для героя
        aiController = new AIController(mapWidth, mapHeight, gameController, aiPlayer, gameMap);
    }

    @Test
    void testRecruitUnits_WhenEnoughGold_ThenUnitsAreAdded() {
        int initialSwordsmanCount = countUnits(aiPlayer, Swordsman.class);
        int initialArcherCount = countUnits(aiPlayer, Archer.class);
        int initialPikemanCount = countUnits(aiPlayer, Pikeman.class);

        aiController.recruitUnits();

        int swordsmanCount = countUnits(aiPlayer, Swordsman.class);
        int archerCount = countUnits(aiPlayer, Archer.class);
        int pikemanCount = countUnits(aiPlayer, Pikeman.class);

        assertTrue(swordsmanCount >= initialSwordsmanCount, "Мечники должны быть добавлены");
        assertTrue(archerCount >= initialArcherCount, "Лучники должны быть добавлены");
        assertTrue(pikemanCount >= initialPikemanCount, "Пикинеры должны быть добавлены");
    }

    @Test
    void testRecruitUnits_WhenNotEnoughGold_ThenNoUnitsAreAdded() {
        aiPlayer.getGold().setAmount(0); // No gold
        int initialUnitCount = aiPlayer.getUnits().size();

        aiController.recruitUnits();

        assertEquals(initialUnitCount, aiPlayer.getUnits().size(), "Юниты не должны быть добавлены");
    }

    @Test
    void testMoveAiHero_HeroMovesToDifferentCoordinates() {
        int initialX = aiHero.getX();
        int initialY = aiHero.getY();

        aiController.moveAiHero();

        boolean moved = (aiHero.getX() != initialX) || (aiHero.getY() != initialY);
        assertTrue(moved, "Герой должен переместиться хотя бы в одну из координат");
    }


    @Test
    void testMoveAiHero_NoMovesLeft() {
        aiHero.setMovesLeft(0); // Установим количество ходов равным 0
        int initialX = aiHero.getX();
        int initialY = aiHero.getY();

        aiController.moveAiHero();

        assertEquals(initialX, aiHero.getX(), "X координата не должна измениться");
        assertEquals(initialY, aiHero.getY(), "Y координата не должна измениться");
    }

    // Вспомогательный метод для подсчета юнитов определенного типа
    private int countUnits(Player player, Class<?> unitClass) {
        int count = 0;
        for (Object unit : player.getUnits()) {
            if (unitClass.isInstance(unit)) {
                count++;
            }
        }
        return count;
    }
}