package model.characters.appearance;

import model.races.*;

public class ShouldersFactory {
    public static String[] shoulderNames = new String[]{"Race Default", "Normal", "Broad", "Slender F", "Narrow F", "Narrow M", "Hunky"};

    public static Shoulders makeShoulders(String key) {
        for (int i = 0; i < shoulderNames.length; ++i) {
            if (shoulderNames[i].equals(key)) {
                switch (i) {
                    case 0:
                        return null;
                    case 1:
                        return new NormalShoulders();
                    case 2:
                        return new BroadShoulders();
                    case 3:
                        return new SlenderShoulders();
                    case 4:
                        return new NarrowShoulders(true);
                    case 5:
                        return new NarrowShoulders(false);
                    case 6:
                        return new HunkyShoulders();
                }
            }
        }
        return null;
    }
}
