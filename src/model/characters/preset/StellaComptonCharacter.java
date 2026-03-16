package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.ArdhCastle;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.SPY;
import static model.races.Race.HALFLING;

public class StellaComptonCharacter extends PresetCharacter {
    public StellaComptonCharacter() {
        super("Stella", "Compton", HALFLING, BKN,
                new StellaCompton(), new CharacterClass[]{BKN, BRD, ASN, SPY},
                ArdhCastle.CASTLE_NAME, List.of(PersonalityTrait.aggressive, PersonalityTrait.romantic, PersonalityTrait.narcissistic),
                "A testy and self-centered halfling who has worked as an agent for the lord of");
    }
}
