package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.SaintQuellinTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.MIN;
import static model.races.Race.HALF_ORC;

public class PuyetGranthamCharacter extends PresetCharacter {
    public PuyetGranthamCharacter() {
        super("Puyet", "Grantham", HALF_ORC, ART,
                new PuyetGrantham(), new CharacterClass[]{ART, ASN, FOR, MIN},
                SaintQuellinTown.NAME, List.of(PersonalityTrait.stingy, PersonalityTrait.greedy, PersonalityTrait.friendly));
    }
}
