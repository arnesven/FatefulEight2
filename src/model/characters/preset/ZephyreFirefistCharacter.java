package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;

import static model.classes.Classes.*;
import static model.classes.Classes.MAG;
import static model.races.Race.DARK_ELF;

public class ZephyreFirefistCharacter extends model.characters.GameCharacter {
    public ZephyreFirefistCharacter() {
        super("Zephyra", "Firefist", DARK_ELF, FOR,
                new ZephyreFirefist(), new CharacterClass[]{FOR, AMZ, DRU, MAG});
        addToPersonality(PersonalityTrait.prude);
    }
}
