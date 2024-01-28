package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;

import static model.classes.Classes.*;
import static model.classes.Classes.MAG;
import static model.races.Race.HIGH_ELF;

public class AlewynSolethalCharacter extends model.characters.GameCharacter {
    public AlewynSolethalCharacter() {
        super("Alewyn", "Solethal", HIGH_ELF, ART,
                new AlewynSolethal(), new CharacterClass[]{ART, WIT, AMZ, MAG});
        addToPersonality(PersonalityTrait.jovial);
        addToPersonality(PersonalityTrait.diplomatic);
        addToPersonality(PersonalityTrait.greedy);
    }
}
