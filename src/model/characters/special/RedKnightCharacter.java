package model.characters.special;

import model.characters.GameCharacter;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.races.Race;

public class RedKnightCharacter extends GameCharacter {
    public RedKnightCharacter() {
        super("Red Knight", "", Race.NORTHERN_HUMAN, Classes.RED_KNIGHT, new RedKnightAppearance(),
                new CharacterClass[]{Classes.RED_KNIGHT, Classes.None, Classes.None, Classes.None});
    }

    public boolean canChangeClothing() {
        return false;
    }

    public boolean canChangeAccessory() {
        return false;
    }

}
