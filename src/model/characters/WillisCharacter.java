package model.characters;

import model.classes.CharacterClass;
import model.classes.Classes;
import model.items.Equipment;
import model.items.accessories.FancyGloves;
import model.items.clothing.PilgrimsCloak;
import model.items.weapons.MagesStaff;
import model.races.Race;

public class WillisCharacter extends GameCharacter {
    public WillisCharacter() {
        super("Willis", "Johanssen", Race.DARK_ELF, Classes.WIZ, new MialeeSeverin(),
                new CharacterClass[]{Classes.WIZ, Classes.WIZ, Classes.WIZ, Classes.WIZ},
                new Equipment(new MagesStaff(), new PilgrimsCloak(), new FancyGloves()));
    }
}
