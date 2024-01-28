package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;

import static model.classes.Classes.*;
import static model.classes.Classes.MAR;
import static model.races.Race.HIGH_ELF;

public class MelethainGauthCharacter extends model.characters.GameCharacter {
    public MelethainGauthCharacter() {
        super("Melethain", "Gauth", HIGH_ELF, BBN,
                new MelethainGauth(), new CharacterClass[]{BBN, THF, ASN, MAR});
        addToPersonality(PersonalityTrait.aggressive);
        addToPersonality(PersonalityTrait.mischievous);
    }
}
