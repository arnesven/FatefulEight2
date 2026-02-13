package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.AckervilleTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.WIZ;
import static model.races.Race.DWARF;

public class EthelthaneVeldtCharacter extends PresetCharacter {
    public EthelthaneVeldtCharacter() {
        super("Ethelthane", "Veldt", DWARF, DRU,
                new EthelthaneVeldt(), new CharacterClass[]{DRU, NOB, BRD, WIZ},
                AckervilleTown.NAME, List.of(PersonalityTrait.cowardly, PersonalityTrait.forgiving, PersonalityTrait.calm));
    }
}
