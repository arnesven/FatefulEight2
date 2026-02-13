package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.CapePaxtonTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.SPY;
import static model.races.Race.WOOD_ELF;

public class LianaClearwaterCharacter extends PresetCharacter {
    public LianaClearwaterCharacter() {
        super("Liana", "Clearwater", WOOD_ELF, PAL,
                new LianaClearwater(), new CharacterClass[]{PAL, CAP, WIZ, SPY},
                CapePaxtonTown.NAME, List.of(PersonalityTrait.encouraging, PersonalityTrait.diplomatic, PersonalityTrait.prudish));
    }
}
