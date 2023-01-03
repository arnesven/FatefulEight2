package model.characters;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.Beard;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.CharacterEyes;
import model.races.Race;
import view.MyColors;
import view.sprites.Sprite8x8;

public class KruskTalandro extends AdvancedAppearance {
    public KruskTalandro() {
        super(Race.HALF_ORC, false, MyColors.DARK_GRAY, 7, 2,
                new CharacterEyes(1), null, new Beard(0x09));
    }

    @Override
    protected void specialization() {
        super.specialization();
        getSprite(3, 4).setColor4(MyColors.BEIGE);
    }

    @Override
    public void applyFacialHair(Race race) {
        super.applyFacialHair(race);
        for (int i = 0; i < 3; ++i) {
            Sprite8x8 left = new Sprite8x8("glasses", "clothes.png", 0x3A+i);
            left.setColor1(MyColors.GRAY);
            addSpriteOnTop(2+i, 3, left);
        }
    }

    @Override
    public CharacterAppearance copy() {
        return new KruskTalandro();
    }
}
