package model.classes.npcs;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.EvilStaffDetail;
import model.classes.Looks;
import view.MyColors;

public class WarlockNPCClass extends NPCClass {
    private final EvilStaffDetail staff;

    public WarlockNPCClass() {
        super("Warlock");
        this.staff = new EvilStaffDetail(MyColors.DARK_GRAY, MyColors.PURPLE);
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnRobe(characterAppearance, MyColors.DARK_PURPLE, MyColors.DARK_GREEN);
        Looks.putOnHood(characterAppearance, MyColors.DARK_PURPLE);
        if (characterAppearance instanceof AdvancedAppearance) {
            staff.applyYourself((AdvancedAppearance) characterAppearance,
                    characterAppearance.getRace());
        }
    }

    @Override
    public boolean coversEars() {
        return true;
    }
}
