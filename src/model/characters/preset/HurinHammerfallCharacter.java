package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.SouthMeadhomeTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.CAP;
import static model.races.Race.DWARF;

public class HurinHammerfallCharacter extends PresetCharacter {
    public HurinHammerfallCharacter() {
        super("Hurin", "Hammerfall", DWARF, ART,
                new HurinHammerfall(), new CharacterClass[]{ART, PAL, THF, CAP},
                SouthMeadhomeTown.NAME, List.of(PersonalityTrait.encouraging, PersonalityTrait.friendly, PersonalityTrait.calm));
    }
}
