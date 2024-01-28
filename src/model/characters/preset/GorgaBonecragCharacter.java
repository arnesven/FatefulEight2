package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;

import static model.classes.Classes.*;
import static model.classes.Classes.WIZ;
import static model.races.Race.HALF_ORC;

public class GorgaBonecragCharacter extends model.characters.GameCharacter {
    public GorgaBonecragCharacter() {
        super("Gorga", "Bonecrag", HALF_ORC, BKN,
                new GorgaBonecrag(), new CharacterClass[]{BKN, AMZ, SPY, WIZ});
        addToPersonality(PersonalityTrait.rude);
        addToPersonality(PersonalityTrait.cold);
        addToPersonality(PersonalityTrait.irritable);
    }
}
