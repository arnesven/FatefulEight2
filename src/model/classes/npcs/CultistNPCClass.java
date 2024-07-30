package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.classes.normal.AssassinClass;
import model.classes.npcs.NPCClass;
import view.MyColors;

public class CultistNPCClass extends NPCClass {
    protected CultistNPCClass() {
        super("Cultist");
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnRobe(characterAppearance, MyColors.DARK_BLUE, MyColors.DARK_GREEN);
        Looks.putOnHood(characterAppearance, MyColors.DARK_BLUE);
        Looks.putOnMask(characterAppearance, MyColors.DARK_BLUE);
    }

    @Override
    public boolean coversEars() {
        return true;
    }

    @Override
    public boolean showFacialHair() {
        return false;
    }
}
