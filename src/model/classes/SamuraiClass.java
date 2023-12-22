package model.classes;

import model.characters.appearance.CharacterAppearance;
import view.MyColors;
import view.sprites.ClothesSprite;
import view.sprites.PortraitSprite;

public class SamuraiClass extends CaptainClass {

    private static final MyColors ARMOR_COLOR = MyColors.DARK_RED;
    private static final MyColors SECONDARY_COLOR = MyColors.GRAY_RED;

    protected SamuraiClass() {
        super("Samurai", "SAM");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnTunic(characterAppearance, SECONDARY_COLOR);
        Looks.putOnLightArmor(characterAppearance, ARMOR_COLOR, SECONDARY_COLOR);
        putOnSamuraiHelm(characterAppearance);
    }

    private void putOnSamuraiHelm(CharacterAppearance characterAppearance) {
        characterAppearance.removeOuterHair();
        for (int y = 0; y <= 4; ++y) {
            for (int x = 1; x <= 5; ++x) {
                PortraitSprite spr = new ClothesSprite(0x130 + 0x10 * y + x + 10, ARMOR_COLOR, SECONDARY_COLOR);
                if (y < 3) {
                    spr.setColor4(MyColors.RED);
                } else {
                    spr.setColor4(MyColors.DARK_GRAY);
                }
                if ((y == 3 && 2 <= x && x <= 4) || y > 3) {
                    characterAppearance.addSpriteOnTop(x, y, spr);
                } else {
                    characterAppearance.setSprite(x, y, spr);
                }
            }
        }
    }

}
