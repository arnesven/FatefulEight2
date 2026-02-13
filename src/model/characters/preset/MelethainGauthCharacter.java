package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.EbonshireTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.MAR;
import static model.races.Race.HIGH_ELF;

public class MelethainGauthCharacter extends PresetCharacter {
    public MelethainGauthCharacter() {
        super("Melethain", "Gauth", HIGH_ELF, BBN,
                new MelethainGauth(), new CharacterClass[]{BBN, THF, ASN, MAR},
                EbonshireTown.NAME, List.of(PersonalityTrait.aggressive, PersonalityTrait.mischievous, PersonalityTrait.romantic));
    }
}
