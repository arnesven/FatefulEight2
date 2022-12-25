package util;

import java.util.List;
import java.util.Random;

public class MyRandom {
    private static Random random = new Random();
    public static int randInt(int range) {
        return random.nextInt(range);
    }

    public static int rollD10() {
        return randInt(10) + 1;
    }

    public static <T> T sample(List<? extends T> list) {
        return list.get(MyRandom.randInt(list.size()));
    }

    public static int rollD6() {
        return randInt(6)+1;
    }

    public static int randInt(int a, int b) {
        return a + randInt(b - a + 1);
    }
}
