package model.enemies;

import model.characters.GameCharacter;
import model.characters.appearance.RandomAppearance;
import model.classes.Classes;
import model.items.Equipment;
import model.items.accessories.SkullCap;
import model.items.clothing.LeatherArmor;
import model.items.weapons.Spear;
import model.races.Race;

public class SpearmanCharacter extends GameCharacter {
    public SpearmanCharacter(String firstName, String lastName, Race race) {
        super(firstName, lastName, race, Classes.CAP, new RandomAppearance(race),
                new Equipment(new Spear(), new LeatherArmor(), new SkullCap()));
    }
}
