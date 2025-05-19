package model.characters.appearance;

import model.races.Race;
import view.MyColors;

public class CrossInHandDetail extends StaffHandDetail {
    public CrossInHandDetail() {
        super(MyColors.LIGHT_GRAY, MyColors.GRAY);
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
                addSpriteOnTop(appearance, 0x188 + y * 0x10 + x, x, 3 + y, race.getColor());
            }
        }
    }
}
