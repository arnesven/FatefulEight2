package model.characters.appearance;

import model.races.Race;
import view.MyColors;

public class GatheredBeard extends Beard {
    private final MyColors lineColor;
    private final MyColors orbColor;
    private final boolean full;

    public GatheredBeard(MyColors lineColor, MyColors orbColor, boolean full) {
        super(full ? 4 : 0, 0x44);
        this.lineColor = lineColor;
        this.orbColor = orbColor;
        this.full = full;
    }

    @Override
    public boolean meetsSideburns() {
        return full;
    }

    @Override
    public void apply(AdvancedAppearance appearance, Race race) {
        for (int y = 4; y < 7; ++y) {
            for (int x = 0; x < 3; ++x) {
                if (x != 1 || y != 4) {
                    setSpriteOnTop(appearance, 0x243 + 0x10 * y + x, x + 2, y, lineColor, orbColor);
                }
            }
        }
    }
}
