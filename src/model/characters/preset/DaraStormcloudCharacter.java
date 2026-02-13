package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.EbonshireTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.MAR;
import static model.races.Race.WOOD_ELF;

public class DaraStormcloudCharacter extends PresetCharacter {
    public DaraStormcloudCharacter() {
        super("Dara", "Stormcloud", WOOD_ELF, ASN,
                new DaraStormcloud(), new CharacterClass[]{FOR, ASN, BRD, MAR},
                EbonshireTown.NAME, List.of(PersonalityTrait.greedy, PersonalityTrait.critical, PersonalityTrait.lawful));
    }
}
