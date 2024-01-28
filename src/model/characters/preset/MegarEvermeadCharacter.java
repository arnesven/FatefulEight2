package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;

import static model.classes.Classes.*;
import static model.classes.Classes.MIN;
import static model.races.Race.DWARF;

public class MegarEvermeadCharacter extends model.characters.GameCharacter {
    public MegarEvermeadCharacter() {
        super("Megar", "Evermead", DWARF, ART,
                new MegarEvermead(), new CharacterClass[]{ART, BKN, PAL, MIN});
        addToPersonality(PersonalityTrait.benevolent);
        addToPersonality(PersonalityTrait.friendly);
    }
}
