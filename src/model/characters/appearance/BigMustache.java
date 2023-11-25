package model.characters.appearance;

import model.races.Race;
import view.MyColors;

public class BigMustache extends Beard {
    private final MyColors lineColor;
    public BigMustache(MyColors color) {
        super(0, 0x41);
        lineColor = color;
    }

    @Override
    public void apply(AdvancedAppearance appearance, Race race) {
        for (int y = 4; y < 6; ++y) {
            for (int x = 2; x < 5; ++x) {
                setSpriteOnTop(appearance, 0x18A + 0x10 * y + x, x, y, lineColor);
            }
        }
    }
}
