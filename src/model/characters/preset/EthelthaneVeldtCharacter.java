package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.AckervilleTown;

import static model.classes.Classes.*;
import static model.classes.Classes.WIZ;
import static model.races.Race.DWARF;

public class EthelthaneVeldtCharacter extends model.characters.GameCharacter {
    public EthelthaneVeldtCharacter() {
        super("Ethelthane", "Veldt", DWARF, DRU,
                new EthelthaneVeldt(), new CharacterClass[]{DRU, NOB, BRD, WIZ});
        addToPersonality(PersonalityTrait.cowardly);
        addToPersonality(PersonalityTrait.forgiving);
        addToPersonality(PersonalityTrait.calm);
        setHomeTown(AckervilleTown.NAME);
    }
}
