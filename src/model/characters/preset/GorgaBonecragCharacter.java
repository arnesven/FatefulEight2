package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.SaintQuellinTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.WIZ;
import static model.races.Race.HALF_ORC;

public class GorgaBonecragCharacter extends PresetCharacter {
    public GorgaBonecragCharacter() {
        super("Gorga", "Bonecrag", HALF_ORC, BKN,
                new GorgaBonecrag(), new CharacterClass[]{BKN, AMZ, SPY, WIZ},
                SaintQuellinTown.NAME, List.of(PersonalityTrait.rude, PersonalityTrait.cold, PersonalityTrait.irritable));
    }
}
