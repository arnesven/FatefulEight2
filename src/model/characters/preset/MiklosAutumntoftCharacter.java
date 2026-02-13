package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.AshtonshireTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.races.Race.HIGH_ELF;

public class MiklosAutumntoftCharacter extends PresetCharacter {
    public MiklosAutumntoftCharacter() {
        super("Miklos", "Autumntoft", HIGH_ELF, NOB,
                new MiklosAutumntoft(), new CharacterClass[]{CAP, NOB, PAL, PRI},
                AshtonshireTown.NAME, List.of(PersonalityTrait.narcissistic, PersonalityTrait.playful, PersonalityTrait.mischievous));
    }
}
