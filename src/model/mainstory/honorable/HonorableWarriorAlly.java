package model.mainstory.honorable;

import model.characters.GameCharacter;
import model.characters.appearance.RandomAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.items.Equipment;
import model.items.clothing.PlateMailArmor;
import model.items.weapons.Longsword;
import model.races.AllRaces;

public class HonorableWarriorAlly extends GameCharacter {
    public HonorableWarriorAlly(CharacterClass cls) {
        super("Honorable", "Warrior",AllRaces.EASTERN_HUMAN, cls, new RandomAppearance(AllRaces.EASTERN_HUMAN),
                Classes.NO_OTHER_CLASSES,
                new Equipment(new Longsword(), new PlateMailArmor(), null));
        setLevel(3);
    }
}
