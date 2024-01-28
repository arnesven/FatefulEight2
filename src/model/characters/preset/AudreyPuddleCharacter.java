package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;

import static model.classes.Classes.*;
import static model.classes.Classes.ART;
import static model.races.Race.HALFLING;

public class AudreyPuddleCharacter extends model.characters.GameCharacter {
    public AudreyPuddleCharacter() {
        super("Audrey", "Puddle", HALFLING, SPY,
                new AudreyPuddle(), new CharacterClass[]{BRD, SPY, AMZ, ART});
        addToPersonality(PersonalityTrait.cowardly);
        addToPersonality(PersonalityTrait.playful);
        addToPersonality(PersonalityTrait.encouraging);
    }
}
