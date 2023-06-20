package model.characters.appearance;

import model.races.Race;
import view.MyColors;
import view.sprites.FilledBlockSprite;

public class ScruffyBeard extends Beard {
    private final MyColors lineColor;
    public ScruffyBeard(MyColors color) {
        super(0, 0x45);
        this.lineColor = color;
    }

    @Override
    public void apply(AdvancedAppearance appearance, Race race) {
        for (int y = 4; y < 7; ++y) {
            for (int x = 2; x < 5; ++x) {
                if (x != 3 || y != 4) {
                    setSpriteOnTop(appearance, 0xF5 + 0x10 * y + x, x, y, lineColor);
                }
            }
        }
    }
}
