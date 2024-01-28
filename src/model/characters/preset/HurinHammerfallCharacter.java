package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;

import static model.classes.Classes.*;
import static model.classes.Classes.CAP;
import static model.races.Race.DWARF;

public class HurinHammerfallCharacter extends model.characters.GameCharacter {
    public HurinHammerfallCharacter() {
        super("Hurin", "Hammerfall", DWARF, ART,
                new HurinHammerfall(), new CharacterClass[]{ART, PAL, THF, CAP});
        addToPersonality(PersonalityTrait.encouraging);
    }
}
