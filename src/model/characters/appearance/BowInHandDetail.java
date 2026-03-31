package model.characters.appearance;

import model.classes.Looks;
import model.races.Race;
import view.MyColors;

public class BowInHandDetail extends StaffHandDetail {
    public BowInHandDetail() {
        super(MyColors.BROWN, MyColors.GRAY);
    }

    @Override
    public void applyYourself(AdvancedAppearance appearance, Race race) {
        MyColors skinColor = Looks.getSkinColorToUse(appearance);
        addSpriteOnTop(appearance, 0x187, 0, 5, skinColor);
        addSpriteOnTop(appearance, 0x174, 0, 6, skinColor);
        addSpriteOnTop(appearance, 0x165, 1, 5, skinColor);
        addSpriteOnTop(appearance, 0x175, 1, 6, skinColor);
        for (int y = 0; y < 2; ++y) {
            for (int x = 0; x < 2; ++x) {
                addSpriteOnTop(appearance, 0x184 + y * 0x10 + x, x, 3 + y, skinColor);
            }
        }
        for (int x = 0; x < 2; ++x) {
            addSpriteOnTop(appearance, 0x184 + x, x, 2, skinColor);
        }
        addSpriteOnTop(appearance, 0x186, 0, 0, skinColor);
        addSpriteOnTop(appearance, 0x196, 0, 1, skinColor);
        addSpriteOnTop(appearance, 0x197, 1, 1, skinColor);
    }
}
