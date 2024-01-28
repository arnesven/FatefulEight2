package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;

import static model.classes.Classes.*;
import static model.classes.Classes.MAR;
import static model.races.Race.HALF_ORC;

public class BazUrGhanCharacter extends model.characters.GameCharacter {
    public BazUrGhanCharacter() {
        super("Baz", "Ur-Ghan", HALF_ORC, AMZ,
                new BazUrGhan(), new CharacterClass[]{AMZ, BRD, FOR, MAR});
        addToPersonality(PersonalityTrait.narcissistic);
        addToPersonality(PersonalityTrait.gluttonous);
        addToPersonality(PersonalityTrait.cowardly);
    }
}
