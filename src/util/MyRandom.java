package util;

import model.races.Race;
import view.MyColors;

import java.util.*;

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

    public static <T> List<T> sampleN(int n, List<? extends T> list) {
        List<T> result = new ArrayList<>(list);
        Collections.shuffle(result);
        for (int i = n; i < list.size(); ++i) {
            result.remove(0);
        }
        return result;
    }

    public static int rollD6() {
        return randInt(6)+1;
    }

    public static int randInt(int a, int b) {
        return a + randInt(b - a + 1);
    }

    public static String randomGender() {
        if (random.nextBoolean()) {
            return "he";
        }
        return "she";
    }

    public static MyColors nextColor() {
        return sample(Arrays.asList(MyColors.values()));
    }

    public static boolean flipCoin() {
        return random.nextBoolean();
    }

    public static Race nextRace() {
        return sample(Arrays.asList(Race.allRaces));
    }

    public static double nextDouble() {
        return random.nextDouble();
    }
}
