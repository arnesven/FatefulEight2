package model.characters.appearance;

import model.races.Race;
import view.MyColors;

public class SpiralStaffDetail extends StaffHandDetail {

    public SpiralStaffDetail(MyColors staffColor, MyColors orbColor) {
        super(staffColor, orbColor);
    }

    public SpiralStaffDetail() {
        this(MyColors.WHITE, MyColors.CYAN);
    }

    @Override
    public void applyYourself(AdvancedAppearance appearance, Race race) {
        for (int set = 0; set < 3; ++set) {
            for (int y = 0; y < 2; ++y) {
                for (int x = 0; x < 2; ++x) {
                    addSpriteOnTop(appearance, 0x164 + y * 0x10 + x + 2*set, x, 5 + y - 2*set, race.getColor());
                }
            }
        }
    }
}
