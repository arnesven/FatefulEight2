package model.characters;

import model.classes.CharacterClass;
import model.classes.Classes;
import model.races.Race;

import static model.classes.Classes.None;

public class WitchKingCharacter extends GameCharacter {
    public WitchKingCharacter() {
        super("Witch King", "", Race.WITCH_KING, Classes.WITCH_KING,
                new WitchKingAppearance(), new CharacterClass[]{Classes.WITCH_KING, None, None, None});
    }
}
