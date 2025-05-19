package model.characters.appearance;

import model.races.Race;
import view.MyColors;
import view.sprites.ClothesSprite;
import view.sprites.Sprite8x8;

public class SpearInHandDetail extends StaffHandDetail {
    private static final MyColors STRAP_COLOR =  MyColors.BEIGE;

    public SpearInHandDetail() {
        super(MyColors.BROWN, STRAP_COLOR);
    }

    @Override
    public void applyYourself(AdvancedAppearance appearance, Race race) {
        for (int y = 0; y < 2; ++y) {
            for (int x = 0; x < 2; ++x) {
                addSpriteOnTop(appearance, 0x164 + y * 0x10 + x, x, 5 + y, race.getColor());
            }
        }
        for (int x = 0; x < 2; ++x) {
            addSpriteOnTop(appearance, 0x176 + x, x, 4, race.getColor());
        }
        for (int x = 0; x < 2; ++x) {
            addSpriteOnTop(appearance, 0x19A + + x, x, 3, race.getColor());
        }
        for (int x = 0; x < 2; ++x) {
            addNonSkinColorSpriteOnTop(appearance, 0x18A + x, x, 2,
                    MyColors.GRAY, MyColors.DARK_GRAY, STRAP_COLOR);
        }
        for (int y = 0; y < 2; ++y) {
            for (int x = 0; x < 2; ++x) {
                addNonSkinColorSpriteOnTop(appearance, 0x18C + y * 0x10 + x, x, 0 + y,
                        MyColors.GRAY, MyColors.DARK_GRAY, STRAP_COLOR);
            }
        }
    }

    protected void addNonSkinColorSpriteOnTop(AdvancedAppearance appearance, int num, int x, int y,
                                              MyColors color2, MyColors color3, MyColors color4) {
        Sprite8x8 left = new ClothesSprite(num, MyColors.BLACK, color3, color4);
        left.setColor2(color2);
        appearance.addSpriteOnTop(x, y, left);
    }
}
