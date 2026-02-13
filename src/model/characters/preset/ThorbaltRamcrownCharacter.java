package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.BullsVilleTown;
import model.map.locations.LowerThelnTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.BRD;
import static model.races.Race.DWARF;

public class ThorbaltRamcrownCharacter extends PresetCharacter {
    public ThorbaltRamcrownCharacter() {
        super("Thorbalt", "Ramcrown", DWARF, CAP,
                new ThorbaltRamcrown(), new CharacterClass[]{ASN, CAP, MAG, BRD},
                BullsVilleTown.NAME, List.of(PersonalityTrait.narcissistic, PersonalityTrait.critical, PersonalityTrait.friendly));
    }
}
