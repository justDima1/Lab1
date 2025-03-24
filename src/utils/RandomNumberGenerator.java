package utils;

import java.util.Random;

public class RandomNumberGenerator {
    private static final Random random = new Random();

    public static int generate(int bound) {
        return random.nextInt(bound);
    }
}