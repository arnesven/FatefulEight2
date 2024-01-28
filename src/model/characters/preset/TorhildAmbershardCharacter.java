package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import static model.classes.Classes.*;
import static model.classes.Classes.SPY;
import static model.races.Race.DWARF;

public class TorhildAmbershardCharacter extends model.characters.GameCharacter {
    public TorhildAmbershardCharacter() {
        super("Torhild", "Ambershard", DWARF, WIT,
                new TorhildAmbershard(), new CharacterClass[]{WIT, AMZ, MAR, SPY});
        addToPersonality(PersonalityTrait.benevolent);
        addToPersonality(PersonalityTrait.intellectual);
        addToPersonality(PersonalityTrait.mischievous);
    }

}
