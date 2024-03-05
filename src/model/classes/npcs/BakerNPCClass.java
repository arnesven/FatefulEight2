package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import view.MyColors;
import view.sprites.ClothesSpriteWithBack;

public class BakerNPCClass extends NPCClass {
    public BakerNPCClass() {
        super("Baker");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnTunic(characterAppearance, MyColors.WHITE);
        Looks.putOnApron(characterAppearance, MyColors.BROWN, MyColors.WHITE);
        putOnBakersHat(characterAppearance);
    }

    public static void putOnBakersHat(CharacterAppearance characterAppearance) {
        characterAppearance.removeOuterHair();
        for (int y = 0; y <= 2; ++y) {
            for (int x = 2; x <= 4; ++x) {
                characterAppearance.setSprite(x, y,
                        new ClothesSpriteWithBack(0x10 * y + x + 11,
                                MyColors.WHITE, MyColors.LIGHT_GRAY));
            }
        }
        for (int x = 2; x <= 4; ++x) {
            characterAppearance.getSprite(x, 2).setColor4(MyColors.BEIGE);
        }
    }
}
