package model.characters.appearance;

import model.races.Race;
import view.MyColors;

import java.io.Serializable;

public class Beard implements Serializable {

    public static Beard[] allBeards = new Beard[]{
            new Beard(0, 0x00), new Beard(1, 0x40), new Beard(2, 0x00),
            new Beard(3, 0x00), new Beard(4, 0x40), new Beard(5, 0x41),
            new Beard(6, 0x42), new Beard(7, 0x00), new Beard(8, 0x00),
            new Beard(9, 0x00), new Beard(0xA, 0x00), new Beard(0xB, 0x00),
            new Beard(0xC, 0x00), new Beard(new int[]{0xFB, 0xFB}, 0x43), new Beard(new int[]{0xA6, 0xB6}, 0x43),
            new BigBeard(MyColors.BLACK), new BigAndLongBeard(MyColors.BLACK),
            new LongBeard(MyColors.BLACK)};

    private int left = -1;
    private int right = -1;
    private int num = -1;
    private final int avatarSprite;

    public Beard(int num, int avatarSprite) {
        this.num = num;
        this.avatarSprite = avatarSprite;
    }

    public Beard(int[] leftAndRight, int avatarSprite) {
        this.left = leftAndRight[0];
        this.right = leftAndRight[1];
        this.avatarSprite = avatarSprite;
    }

    public int getLeftCheck() {
        if (num == -1) {
            return left;
        }
        return 0x30 + num;
    }

    public int getRightCheek() {
        if (num == -1) {
            return right;
        }
        return 0x40 + num;
    }

    public void apply(AdvancedAppearance advancedAppearance, Race race) {

    }

    public int getAvatarSprite() {
        return avatarSprite;
    }
}
