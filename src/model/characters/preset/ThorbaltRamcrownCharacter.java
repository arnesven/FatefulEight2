package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;

import static model.classes.Classes.*;
import static model.classes.Classes.BRD;
import static model.races.Race.DWARF;

public class ThorbaltRamcrownCharacter extends model.characters.GameCharacter {
    public ThorbaltRamcrownCharacter() {
        super("Thorbalt", "Ramcrown", DWARF, CAP,
                new ThorbaltRamcrown(), new CharacterClass[]{ASN, CAP, MAG, BRD});
        addToPersonality(PersonalityTrait.narcissistic);
        addToPersonality(PersonalityTrait.critical);
    }
}
