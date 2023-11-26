package model.characters.appearance;

import model.races.*;

public class ShouldersFactory {
    public static String[] shoulderNames = new String[]{"Race Default", "Normal", "Broad", "Slender", "Narrow", "Hunky"};

    public static Shoulders makeShoulders(String key, boolean gender) {
        for (int i = 0; i < shoulderNames.length; ++i) {
            if (shoulderNames[i].equals(key)) {
                switch (i) {
                    case 0:
                        return null;
                    case 1:
                        return new NormalShoulders(gender);
                    case 2:
                        return new BroadShoulders(gender);
                    case 3:
                        return new SlenderShoulders(gender);
                    case 4:
                        return new NarrowShoulders(gender);
                    case 5:
                        return new HunkyShoulders(gender);
                }
            }
        }
        return null;
    }
}
