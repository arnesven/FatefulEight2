package model.characters.appearance;

public class HairStyle5x2 extends HairStyle3x2 {
    private final int num;
    private static final int FRAME_TOP = 0x10F;

    public HairStyle5x2(int num, boolean inBack, boolean longInBack, int avatarNormal, int avatarBack) {
        super(num + 0x01, true, true, inBack, longInBack, avatarNormal, avatarBack);
        this.num = num;
    }

    @Override
    public int[] getOuterFrame() {
        return new int[]{num+0x10, num, FRAME_TOP, FRAME_TOP, FRAME_TOP, FRAME_TOP, FRAME_TOP, num+0x04, num+0x14};
    }
}
