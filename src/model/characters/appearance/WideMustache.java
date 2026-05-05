package model.characters.appearance;

import model.races.Race;
import view.MyColors;

public class WideMustache extends Beard {
    public WideMustache() {
        super(0, 0x41);
    }

    @Override
    public boolean meetsSideburns() {
        return false;
    }

    @Override
    public void apply(AdvancedAppearance appearance, Race race) {
        int y = 4;
        for (int x = 2; x < 5; ++x) {
            setSpriteOnTop(appearance, 0x244 + 0x10 * y + x, x, y, MyColors.BLACK);
        }
    }
}
