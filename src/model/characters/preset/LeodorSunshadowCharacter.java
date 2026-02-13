package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.EastDurhamTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.WIZ;
import static model.races.Race.DARK_ELF;

public class LeodorSunshadowCharacter extends PresetCharacter {
    public LeodorSunshadowCharacter() {
        super("Leodor", "Sunshadow", DARK_ELF, NOB,
                new LeodorSunshadow(), new CharacterClass[]{NOB, BKN, BRD, WIZ},
                EastDurhamTown.NAME, List.of(PersonalityTrait.unkind, PersonalityTrait.cold, PersonalityTrait.intellectual));
    }
}
