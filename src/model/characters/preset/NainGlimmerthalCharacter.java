package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.map.locations.RoukonTown;
import model.races.Race;

import java.util.List;

public class NainGlimmerthalCharacter extends PresetCharacter {
    public NainGlimmerthalCharacter() {
        super("Nain", "Glimmerthal", Race.DWARF, Classes.BRD, new NainGlimmerthal(),
                new CharacterClass[]{Classes.BRD, Classes.DRU, Classes.FOR, Classes.MIN},
                RoukonTown.NAME, List.of(PersonalityTrait.jovial, PersonalityTrait.brave, PersonalityTrait.rude),
                "A dwarf with a great sense of humor, sometimes however, at the expense of others, from ");
    }
}
