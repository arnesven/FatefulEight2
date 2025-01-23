package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.ArdhCastle;

import static model.classes.Classes.*;
import static model.classes.Classes.SPY;
import static model.races.Race.HALFLING;

public class StellaComptonCharacter extends model.characters.GameCharacter {
    public StellaComptonCharacter() {
        super("Stella", "Compton", HALFLING, BKN,
                new StellaCompton(), new CharacterClass[]{BKN, BRD, ASN, SPY});
        addToPersonality(PersonalityTrait.aggressive);
        addToPersonality(PersonalityTrait.romantic);
        addToPersonality(PersonalityTrait.narcissistic);
        setHomeTown(ArdhCastle.CASTLE_NAME);
    }
}
