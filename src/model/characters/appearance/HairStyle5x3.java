package model.characters.appearance;

public class HairStyle5x3 extends HairStyle3x2 {
    private final int num;

    public HairStyle5x3(int num, boolean inBack, boolean longInBack, int avatarNormal,
                        int avatarBack, int avatarFullBack, int avatarHalfBack, String description) {
        super(num + 0x11, true, true, inBack, longInBack,
                avatarNormal, avatarBack, avatarFullBack, avatarHalfBack, description);
        this.num = num;
    }

    @Override
    public int[] getOuterFrame() {
        return new int[]{num+0x20, num+0x10, num, num+1, num+2, num+3, num+4, num+0x14, num+0x24};
    }
}
