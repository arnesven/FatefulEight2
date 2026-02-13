package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.UpperThelnTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.MAR;
import static model.races.Race.HALFLING;

public class PaddyWillowbrushCharacter extends PresetCharacter {
    public PaddyWillowbrushCharacter() {
        super("Paddy", "Willowbrush", HALFLING, MIN,
                new PaddyWillowbrush(), new CharacterClass[]{MIN, DRU, FOR, MAR},
                UpperThelnTown.NAME, List.of(PersonalityTrait.jovial, PersonalityTrait.anxious, PersonalityTrait.naive));
    }
}
