package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.CapePaxtonTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.SPY;
import static model.races.Race.DWARF;

public class TorhildAmbershardCharacter extends PresetCharacter {
    public TorhildAmbershardCharacter() {
        super("Torhild", "Ambershard", DWARF, WIT,
                new TorhildAmbershard(), new CharacterClass[]{WIT, AMZ, MAR, SPY},
                CapePaxtonTown.NAME, List.of(PersonalityTrait.benevolent, PersonalityTrait.intellectual, PersonalityTrait.mischievous));
    }

}
