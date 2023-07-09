package model.characters;

import model.characters.appearance.*;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.items.Equipment;
import model.items.accessories.FancyGloves;
import model.items.clothing.PilgrimsCloak;
import model.items.weapons.MagesStaff;
import model.races.Race;
import view.MyColors;

public class WillisCharacter extends GameCharacter {
    public WillisCharacter() {
        super("Willis", "Johanssen", Race.DARK_ELF, Classes.ARCANIST, new WillisJohanssen(),
                new CharacterClass[]{Classes.ARCANIST, Classes.None, Classes.None, Classes.None},
                new Equipment(new MagesStaff(), new PilgrimsCloak(), new FancyGloves()));
    }

    private static class WillisJohanssen extends AdvancedAppearance {
        public WillisJohanssen() {
            super(Race.DARK_ELF, true, MyColors.DARK_PURPLE,
                    0, 10, CharacterEyes.allEyes[4], HairStyle.allHairStyles[2],
                    Beard.allBeards[0]);
        }
    }
}
