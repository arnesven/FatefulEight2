package model.characters.appearance;

import model.races.Race;
import view.MyColors;

public class BowInHandDetail extends StaffHandDetail {
    public BowInHandDetail() {
        super(MyColors.BROWN, MyColors.GRAY);
    }

    @Override
    public void applyYourself(AdvancedAppearance appearance, Race race) {
        addSpriteOnTop(appearance, 0x187, 0, 5, race.getColor());
        addSpriteOnTop(appearance, 0x174, 0, 6, race.getColor());
        addSpriteOnTop(appearance, 0x165, 1, 5, race.getColor());
        addSpriteOnTop(appearance, 0x175, 1, 6, race.getColor());
        for (int y = 0; y < 2; ++y) {
            for (int x = 0; x < 2; ++x) {
                addSpriteOnTop(appearance, 0x184 + y * 0x10 + x, x, 3 + y, race.getColor());
            }
        }
        for (int x = 0; x < 2; ++x) {
            addSpriteOnTop(appearance, 0x184 + x, x, 2, race.getColor());
        }
        addSpriteOnTop(appearance, 0x186, 0, 0, race.getColor());
        addSpriteOnTop(appearance, 0x196, 0, 1, race.getColor());
        addSpriteOnTop(appearance, 0x197, 1, 1, race.getColor());
    }
}
