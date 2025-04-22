package model.classes.prestige;

import model.characters.appearance.CharacterAppearance;
import model.classes.normal.CaptainClass;
import model.classes.Looks;
import view.MyColors;
import view.sprites.ClothesSprite;
import view.sprites.PortraitSprite;

public class SamuraiClass extends CaptainClass { // TODO: Extend Prestige class instead

    private static final MyColors DEFAULT_ARMOR_COLOR = MyColors.DARK_RED;
    private static final MyColors DEFAULT_SECONDARY_COLOR = MyColors.GRAY_RED;
    private static final MyColors DEFAULT_DETAIL_COLOR = MyColors.RED;
    private final MyColors armorColor;
    private final MyColors secondaryColor;
    private final MyColors helmetDetail;

    public SamuraiClass(MyColors armorColor, MyColors secondaryColor, MyColors helmetDetail) {
        super("Samurai", "SAM");
        this.armorColor = armorColor;
        this.secondaryColor = secondaryColor;
        this.helmetDetail = helmetDetail;
    }

    public SamuraiClass() {
        this(DEFAULT_ARMOR_COLOR, DEFAULT_SECONDARY_COLOR, DEFAULT_DETAIL_COLOR);
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnTunic(characterAppearance, secondaryColor);
        Looks.putOnLightArmor(characterAppearance, armorColor, secondaryColor);
        putOnSamuraiHelm(characterAppearance);
    }

    private void putOnSamuraiHelm(CharacterAppearance characterAppearance) {
        characterAppearance.removeOuterHair();
        for (int y = 0; y <= 4; ++y) {
            for (int x = 1; x <= 5; ++x) {
                PortraitSprite spr = new ClothesSprite(0x130 + 0x10 * y + x + 10, armorColor, secondaryColor);
                if (y < 3) {
                    spr.setColor4(helmetDetail);
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
