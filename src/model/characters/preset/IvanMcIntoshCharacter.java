package model.characters.preset;

import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.SheffieldTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.MIN;
import static model.races.Race.NORTHERN_HUMAN;

public class IvanMcIntoshCharacter extends PresetCharacter {
    public IvanMcIntoshCharacter() {
        super("Ivan", "McIntosh", NORTHERN_HUMAN, PRI,
                new IvanMcIntosh(), new CharacterClass[]{PRI, BKN, FOR, MIN},
                SheffieldTown.NAME, List.of(PersonalityTrait.generous, PersonalityTrait.forgiving, PersonalityTrait.calm));
    }
}
