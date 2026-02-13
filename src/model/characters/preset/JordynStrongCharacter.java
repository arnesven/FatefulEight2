package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.BullsVilleTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.PRI;
import static model.races.Race.SOUTHERN_HUMAN;

public class JordynStrongCharacter extends PresetCharacter {
    public JordynStrongCharacter() {
        super("Jordyn", "Strong", SOUTHERN_HUMAN, AMZ,
                new JordynStrong(), new CharacterClass[]{AMZ, BRD, FOR, PRI},
                BullsVilleTown.NAME, List.of(PersonalityTrait.benevolent, PersonalityTrait.forgiving, PersonalityTrait.lawful));
    }
}
