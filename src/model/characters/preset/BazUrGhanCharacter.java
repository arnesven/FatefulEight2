package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.ArkvaleCastle;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.MAR;
import static model.races.Race.HALF_ORC;

public class BazUrGhanCharacter extends PresetCharacter {
    public BazUrGhanCharacter() {
        super("Baz", "Ur-Ghan", HALF_ORC, AMZ,
                new BazUrGhan(), new CharacterClass[]{AMZ, BRD, FOR, MAR},
                ArkvaleCastle.CASTLE_NAME,
                List.of(PersonalityTrait.narcissistic, PersonalityTrait.gluttonous, PersonalityTrait.cowardly));
    }
}
