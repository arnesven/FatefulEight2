package model.characters.appearance;

import model.races.Race;
import view.MyColors;

public class ShaggyBeard extends Beard {
    private final MyColors lineColor;
    public ShaggyBeard(MyColors color) {
        super(0, 0x45);
        lineColor = color;
    }

    @Override
    public void apply(AdvancedAppearance appearance, Race race) {
        for (int y = 4; y < 6; ++y) {
            for (int x = 2; x < 5; ++x) {
                setSpriteOnTop(appearance, 0x187 + 0x10 * y + x, x, y, lineColor);
            }
        }
    }

}
