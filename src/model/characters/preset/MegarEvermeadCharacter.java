package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.RoukonTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.MIN;
import static model.races.Race.DWARF;

public class MegarEvermeadCharacter extends PresetCharacter {
    public MegarEvermeadCharacter() {
        super("Megar", "Evermead", DWARF, ART,
                new MegarEvermead(), new CharacterClass[]{ART, BKN, PAL, MIN},
                RoukonTown.NAME, List.of(PersonalityTrait.benevolent, PersonalityTrait.brave, PersonalityTrait.irritable));
    }
}
