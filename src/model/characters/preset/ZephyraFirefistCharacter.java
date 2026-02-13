package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.RoukonTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.MAG;
import static model.races.Race.DARK_ELF;

public class ZephyraFirefistCharacter extends PresetCharacter {
    public ZephyraFirefistCharacter() {
        super("Zephyra", "Firefist", DARK_ELF, FOR,
                new ZephyraFirefist(), new CharacterClass[]{FOR, AMZ, DRU, MAG},
                RoukonTown.NAME, List.of(PersonalityTrait.prudish, PersonalityTrait.diplomatic, PersonalityTrait.generous));
    }
}
