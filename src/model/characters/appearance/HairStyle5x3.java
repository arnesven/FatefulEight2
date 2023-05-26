package model.characters.appearance;

public class HairStyle5x3 extends HairStyle3x2 {
    private final int num;

    public HairStyle5x3(int num, int avatarNormal, int avatarBack) {
        super(num + 0x11, true, true, false, false, avatarNormal, avatarBack);
        this.num = num;
    }

    @Override
    public int[] getOuterFrame() {
        return new int[]{num+0x20, num+0x10, num, num+1, num+2, num+3, num+4, num+0x14, num+0x24};
    }
}
