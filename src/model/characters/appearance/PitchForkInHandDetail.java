package model.characters.appearance;

import model.classes.Looks;
import model.races.Race;
import view.MyColors;

public class PitchForkInHandDetail extends StaffHandDetail {
    public PitchForkInHandDetail() {
        super(MyColors.BROWN, MyColors.GRAY);
    }

    @Override
    public void applyYourself(AdvancedAppearance appearance, Race race) {
        MyColors skinColor = Looks.getSkinColorToUse(appearance);
        for (int set = 0; set < 2; ++set) {
            for (int y = 0; y < 2; ++y) {
                for (int x = 0; x < 2; ++x) {
                    addSpriteOnTop(appearance, 0x164 + y * 0x10 + x + 2*set, x, 5 + y - 2*set, skinColor);
                }
            }
        }
        for (int x = 0; x < 2; ++x) {
            addSpriteOnTop(appearance, 0x1A0 + x, x, 2, skinColor);
        }
        for (int y = 0; y < 2; ++y) {
            for (int x = 0; x < 2; ++x) {
                addSpriteOnTop(appearance, 0x18E + y * 0x10 + x, x,  y, skinColor);
            }
        }
    }
}
