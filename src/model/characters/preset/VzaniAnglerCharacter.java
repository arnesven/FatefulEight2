package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.SheffieldTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.races.Race.HALFLING;

public class VzaniAnglerCharacter extends PresetCharacter {
    public VzaniAnglerCharacter() {
        super("Vzani", "Angler", HALFLING, PRI,
                new VzaniAngler(), new CharacterClass[]{WIZ, MAG, PRI, THF},
                SheffieldTown.NAME, List.of(PersonalityTrait.stingy, PersonalityTrait.calm, PersonalityTrait.critical));
    }
}
