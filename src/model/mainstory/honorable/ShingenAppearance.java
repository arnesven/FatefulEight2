package model.mainstory.honorable;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.Beard;
import model.characters.appearance.TopKnotHairStyle;
import model.classes.prestige.SamuraiClass;
import model.races.AllRaces;
import model.races.EasternHuman;
import view.MyColors;

public class ShingenAppearance extends AdvancedAppearance {
    public ShingenAppearance(MyColors factionColor) {
        super(AllRaces.EASTERN_HUMAN, false, MyColors.DARK_GRAY, 0xD, 0x3,
                EasternHuman.EYES.get(1), new TopKnotHairStyle(MyColors.DARK_GREEN,
                        true, "Top Knot/Bald"), new Beard(5, 0x41));
        MyColors armorColor = factionColor;
        MyColors secondaryColor = MyColors.DARK_GRAY;
        MyColors helmetColor = factionColor;
        if (factionColor == MyColors.BLUE) {
            armorColor = MyColors.DARK_BLUE;
        } else if (factionColor == MyColors.DARK_RED) {
            secondaryColor = MyColors.GRAY_RED;
            helmetColor = MyColors.RED;
        } else if (factionColor == MyColors.DARK_GRAY) {
            armorColor = MyColors.GRAY;
            helmetColor = MyColors.LIGHT_GRAY;
        } else if (factionColor == MyColors.DARK_GREEN) {
            helmetColor = MyColors.GREEN;
        } else if (factionColor == MyColors.DARK_PURPLE) {
            helmetColor = MyColors.PURPLE;
        }
        setClass(new SamuraiClass(armorColor, secondaryColor, helmetColor));
    }
}
