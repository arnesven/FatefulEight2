package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.UpperThelnTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.SOR;
import static model.races.Race.HALFLING;

public class FattyGoldenrodCharacter extends PresetCharacter {
    public FattyGoldenrodCharacter() {
        super("Fatty", "Goldenrod", HALFLING, MIN,
                new FattyGoldenrod(), new CharacterClass[]{MIN, BBN, WIT, SOR},
                UpperThelnTown.NAME, List.of(PersonalityTrait.unkind, PersonalityTrait.rude, PersonalityTrait.cold));
    }
}
