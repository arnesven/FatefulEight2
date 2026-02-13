package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.SheffieldTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.DRU;
import static model.races.Race.SOUTHERN_HUMAN;

public class SebastianSmithCharacter extends PresetCharacter {
    public SebastianSmithCharacter() {
        super("Sebastian", "Smith", SOUTHERN_HUMAN, PAL,
                new SebastianSmith(), new CharacterClass[]{PAL, BKN, CAP, DRU},
                SheffieldTown.NAME, List.of(PersonalityTrait.aggressive, PersonalityTrait.mischievous, PersonalityTrait.intellectual));
    }
}
