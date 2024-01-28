package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;

import static model.classes.Classes.*;
import static model.classes.Classes.WIZ;
import static model.races.Race.DWARF;

public class EthelthaneVeldtCharacter extends model.characters.GameCharacter {
    public EthelthaneVeldtCharacter() {
        super("Ethelthane", "Veldt", DWARF, DRU,
                new EthelthaneVeldt(), new CharacterClass[]{DRU, NOB, BRD, WIZ});
        addToPersonality(PersonalityTrait.cowardly);
    }
}
