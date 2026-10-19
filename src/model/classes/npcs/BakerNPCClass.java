package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.states.cardgames.sleeves.CardGameSleeve;
import model.states.cardgames.sleeves.PlainLongSleeve;
import view.MyColors;
import view.sprites.ClothesSpriteWithBack;

public class BakerNPCClass extends NPCClass {
    private static final MyColors TUNIC_COLOR = MyColors.WHITE;

    public BakerNPCClass() {
        super("Baker");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnTunic(characterAppearance, TUNIC_COLOR);
        Looks.putOnApron(characterAppearance, MyColors.BROWN, MyColors.WHITE);
        putOnBakersHat(characterAppearance);
    }

    @Override
    public CardGameSleeve makeCardGameSleeve() {
        return new PlainLongSleeve(TUNIC_COLOR);
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
