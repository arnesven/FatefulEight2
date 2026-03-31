package model.characters.appearance;

import model.classes.Looks;
import model.races.Race;
import view.MyColors;

public class EvilStaffDetail extends StaffHandDetail {

    public EvilStaffDetail(MyColors staffColor, MyColors orbColor) {
        super(staffColor, orbColor);
    }

    public EvilStaffDetail() {
        this(MyColors.DARK_GRAY, MyColors.RED);
    }

    @Override
    public void applyYourself(AdvancedAppearance appearance, Race race) {
        MyColors skinColor = Looks.getSkinColorToUse(appearance);
        for (int y = 0; y < 2; ++y) {
            for (int x = 0; x < 2; ++x) {
                addSpriteOnTop(appearance, 0x164 + y * 0x10 + x, x, 5 + y, skinColor);
            }
        }
        for (int y = 0; y < 2; ++y) {
            for (int x = 0; x < 2; ++x) {
                addSpriteOnTop(appearance, 0x176 + x, x, 3 + y, skinColor);
            }
        }
        for (int y = 0; y < 2; ++y) {
            for (int x = 0; x < 2; ++x) {
                addSpriteOnTop(appearance, 0x180 + y * 0x10 + x, x, 1 + y, skinColor);
            }
        }
    }
}
