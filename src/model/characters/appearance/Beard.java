package model.characters.appearance;

import model.races.Race;
import view.MyColors;

import java.io.Serializable;

public class Beard implements Serializable {

    public static Beard[] allBeards = new Beard[]{
            new Beard(0), new Beard(1), new Beard(2),
            new Beard(3), new Beard(4), new Beard(5),
            new Beard(6), new Beard(7), new Beard(8),
            new Beard(9), new Beard(0xA), new Beard(0xB),
            new Beard(0xC), new Beard(0xFB, 0xFB), new Beard(0xA6, 0xB6),
            new BigBeard(MyColors.BLACK), new BigAndLongBeard(MyColors.BLACK),
            new LongBeard(MyColors.BLACK)};

    private int left = -1;
    private int right = -1;
    private int num = -1;

    public Beard(int num) {
        this.num = num;
    }

    public Beard(int left, int right) {
        this.left = left;
        this.right = right;
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
}
