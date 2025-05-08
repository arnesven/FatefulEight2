package model.mainstory.honorable;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.Beard;
import model.characters.appearance.TopKnotHairStyle;
import model.classes.CharacterClass;
import model.classes.prestige.SamuraiClass;
import model.races.AllRaces;
import model.races.EasternHuman;
import view.MyColors;

public class ShingenAppearance extends AdvancedAppearance {
    public ShingenAppearance(CharacterClass shingenClass) {
        super(AllRaces.EASTERN_HUMAN, false, MyColors.DARK_GRAY, 0xD, 0x3,
                EasternHuman.EYES.get(1), new TopKnotHairStyle(MyColors.DARK_GREEN,
                        true, "Top Knot/Bald"), new Beard(5, 0x41));
        setClass(shingenClass);
    }
}
