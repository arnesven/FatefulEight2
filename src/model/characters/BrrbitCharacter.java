package model.characters;

import model.classes.Classes;
import model.items.Equipment;
import model.items.clothing.LeatherTunic;
import model.items.weapons.UnarmedCombatWeapon;
import model.races.FrogmanAppearance;
import model.races.Race;

public class BrrbitCharacter extends GameCharacter {
    public BrrbitCharacter(String firstName) {
        super(firstName, "", Race.FROGMAN, Classes.FROGMAN, new FrogmanAppearance(), Classes.NO_OTHER_CLASSES,
                new Equipment(new UnarmedCombatWeapon(), new LeatherTunic(), null));
    }
}
