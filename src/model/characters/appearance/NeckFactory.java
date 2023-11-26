package model.characters.appearance;

public class NeckFactory {
    public static String[] neckNames = new String[]{"Race Default", "Normal", "Slender", "Thick", "Hunky"};

    public static TorsoNeck makeNeck(String key) {
        for (int i = 0; i < neckNames.length; ++i) {
            if (neckNames[i].equals(key)) {
                switch (i) {
                    case 0:
                        return null;
                    case 1:
                        return new NormalNeck();
                    case 2:
                        return new SlenderNeck();
                    case 3:
                        return new ThickNeck();
                    case 4:
                        return new HunkyNeck();
                }
            }
        }
        return null;
    }
}
