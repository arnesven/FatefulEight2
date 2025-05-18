package model.characters.appearance;

import model.races.Race;
import view.MyColors;

public class GnarledStaffDetail extends StaffHandDetail {
    public GnarledStaffDetail() {
        super(MyColors.BROWN, MyColors.LIGHT_YELLOW);
    }

    @Override
    public void applyYourself(AdvancedAppearance appearance, Race race) {
        for (int y = 0; y < 2; ++y) {
            for (int x = 0; x < 2; ++x) {
                addSpriteOnTop(appearance, 0x164 + y * 0x10 + x, x, 5 + y, race.getColor());
            }
        }
        for (int y = 0; y < 2; ++y) {
            for (int x = 0; x < 2; ++x) {
                addSpriteOnTop(appearance, 0x166 + x, x, 3 + y, race.getColor());
            }
        }
        for (int y = 0; y < 2; ++y) {
            for (int x = 0; x < 2; ++x) {
                addSpriteOnTop(appearance, 0x182 + y * 0x10 + x, x, 1 + y, race.getColor());
            }
        }
    }
}
