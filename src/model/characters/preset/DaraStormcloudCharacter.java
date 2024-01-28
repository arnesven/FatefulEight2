package model.characters.preset;

import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.CharacterClass;

import static model.classes.Classes.*;
import static model.classes.Classes.MAR;
import static model.races.Race.WOOD_ELF;

public class DaraStormcloudCharacter extends GameCharacter {
    public DaraStormcloudCharacter() {
        super("Dara", "Stormcloud", WOOD_ELF, ASN,
                new DaraStormcloud(), new CharacterClass[]{FOR, ASN, BRD, MAR});
        addToPersonality(PersonalityTrait.greedy);
        addToPersonality(PersonalityTrait.critical);
        addToPersonality(PersonalityTrait.narcissistic);
    }
}
