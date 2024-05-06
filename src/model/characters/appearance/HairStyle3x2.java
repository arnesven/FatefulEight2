package model.characters.appearance;

public class HairStyle3x2 extends HairStyle {
    private final int avatarNormal;
    private final int avatarBack;
    private final int avatarFullBack;
    private final int avatarHalfBack;
    private int[][] hair;

    public HairStyle3x2(int num, boolean inForehead, boolean onTop, boolean inBack, boolean longInBack,
                        int avatarNormal, int avatarBack, int avatarFullBack, int avatarHalfBack, String description) {
        super(inForehead, onTop, inBack, longInBack, description);
        hair = set3x2HairStyleFrom(num);
        this.avatarNormal = avatarNormal;
        this.avatarBack = avatarBack;
        this.avatarFullBack = avatarFullBack;
        this.avatarHalfBack = avatarHalfBack;
    }

    public HairStyle3x2(int num, boolean inForehead, int avatarNormal, int avatarFullBack, int avatarHalfBack, String description) {
        this(num, inForehead, true, false, true, avatarNormal, 0x00, avatarFullBack, avatarHalfBack, description);
    }

    public int getForeheadLeft() {
        return hair[0][1];
    }

    public int getForeheadCenter() {
        return hair[1][1];
    }

    public int getForeheadRight() {
        return hair[2][1];
    }

    public int getHeadTopLeft() {
        return hair[0][0];
    }

    public int getHeadTop() {
        return hair[1][0];
    }

    public int getHeadTopRight() {
        return hair[2][0];
    }

    @Override
    public int getNormalHair() {
        return avatarNormal;
    }

    @Override
    public int getBackHairOnly() {
        return avatarBack;
    }

    @Override
    public int getFullBackHair() {
        return avatarFullBack;
    }

    @Override
    public int getHalfBackHair() {
        return avatarHalfBack;
    }

    private static int[][] set3x2HairStyleFrom(int i) {
        int result[][] = new int[3][2];
        for (int y = 0; y < 2; ++y) {
            for (int x = 0; x < 3; ++x) {
                result[x][y] = 0x10 * y + i+x;
            }
        }
        return result;
    }
}
