package Tests;

import model.map.MapEditor;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapEditorTest {

    @Test
    void testInitializeMapWithDots() {
        MapEditor editor = new MapEditor(5, 5);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                assertEquals('.', editor.getMap()[i][j], "Карта должна быть инициализирована точками");
            }
        }
    }

    @Test
    void testEditMapSymbol() {
        MapEditor editor = new MapEditor(5, 5);
        editor.editMap(0, 0, 'X');
        assertEquals('X', editor.getMap()[0][0], "Символ должен быть изменен");
    }

    @Test
    void testEditMapInvalidCoordinates() {
        MapEditor editor = new MapEditor(5, 5);
        editor.editMap(10, 10, 'X');
        assertEquals('.', editor.getMap()[0][0], "Символ не должен быть изменен из-за некорректных координат");
    }

    @Test
    void testEditMapInvalidSymbol() {
        MapEditor editor = new MapEditor(5, 5);
        editor.editMap(0, 0, ' ');
        assertEquals('.', editor.getMap()[0][0], "Символ не должен быть изменен из-за некорректного символа");
    }
}