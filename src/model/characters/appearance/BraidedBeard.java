package model.characters.appearance;

import model.races.Race;
import view.MyColors;

public class BraidedBeard extends Beard {
    private final MyColors lineColor;
    private final MyColors beadColor;

    public BraidedBeard(MyColors lineColor, MyColors beadColor) {
        super(4, 0x44);
        this.lineColor = lineColor;
        this.beadColor = beadColor;
    }

    @Override
    public void apply(AdvancedAppearance appearance, Race race) {
        for (int y = 4; y < 7; ++y) {
            for (int x = 0; x < 3; ++x) {
                if (x != 1 || y != 4) {
                    setSpriteOnTop(appearance, 0x240 + 0x10 * y + x, x + 2, y, lineColor, beadColor);
                }
            }
        }
    }
}
