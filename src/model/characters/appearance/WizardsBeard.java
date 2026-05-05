package model.characters.appearance;

import model.races.Race;
import view.MyColors;

public class WizardsBeard extends Beard {
    public WizardsBeard() {
        super(4, 0x44);
    }

    @Override
    public void apply(AdvancedAppearance appearance, Race race) {
        setSpriteOnTop(appearance, 0x283, 2, 4, MyColors.BLACK);
        setSpriteOnTop(appearance, 0x285, 4, 4, MyColors.BLACK);
        for (int y = 5; y < 7; ++y) {
            for (int x = 0; x < 3; ++x) {
                setSpriteOnTop(appearance, 0x246 + 0x10 * y + x, x + 2, y, MyColors.BLACK);
            }
        }
    }
}
