package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.CapePaxtonTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.SOR;
import static model.races.Race.HIGH_ELF;

public class AtalyaBalefrostCharacter extends PresetCharacter {
    public AtalyaBalefrostCharacter() {
        super("Atalya", "Balefrost", HIGH_ELF, WIZ,
                new AtalyaBalefrost(), new CharacterClass[]{WIZ, BKN, DRU, SOR},
                CapePaxtonTown.NAME, List.of(PersonalityTrait.cowardly, PersonalityTrait.naive, PersonalityTrait.stingy));
    }
}
