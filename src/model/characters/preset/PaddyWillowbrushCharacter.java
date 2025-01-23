package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.UpperThelnTown;

import static model.classes.Classes.*;
import static model.classes.Classes.MAR;
import static model.races.Race.HALFLING;

public class PaddyWillowbrushCharacter extends model.characters.GameCharacter {
    public PaddyWillowbrushCharacter() {
        super("Paddy", "Willowbrush", HALFLING, MIN,
                new PaddyWillowbrush(), new CharacterClass[]{MIN, DRU, FOR, MAR});
        addToPersonality(PersonalityTrait.jovial);
        addToPersonality(PersonalityTrait.anxious);
        addToPersonality(PersonalityTrait.naive);
        setHomeTown(UpperThelnTown.NAME);
    }
}
