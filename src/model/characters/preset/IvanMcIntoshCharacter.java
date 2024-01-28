package model.characters.preset;

import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.CharacterClass;

import static model.classes.Classes.*;
import static model.classes.Classes.MIN;
import static model.races.Race.NORTHERN_HUMAN;

public class IvanMcIntoshCharacter extends GameCharacter {
    public IvanMcIntoshCharacter() {
        super("Ivan", "McIntosh", NORTHERN_HUMAN, PRI,
                new IvanMcIntosh(), new CharacterClass[]{PRI, BKN, FOR, MIN});
        addToPersonality(PersonalityTrait.generous);
    }
}
