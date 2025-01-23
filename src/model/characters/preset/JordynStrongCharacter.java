package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.BullsVilleTown;

import static model.classes.Classes.*;
import static model.classes.Classes.PRI;
import static model.races.Race.SOUTHERN_HUMAN;

public class JordynStrongCharacter extends model.characters.GameCharacter {
    public JordynStrongCharacter() {
        super("Jordyn", "Strong", SOUTHERN_HUMAN, AMZ,
                new JordynStrong(), new CharacterClass[]{AMZ, BRD, FOR, PRI});
        addToPersonality(PersonalityTrait.benevolent);
        addToPersonality(PersonalityTrait.forgiving);
        addToPersonality(PersonalityTrait.lawful);
        setHomeTown(BullsVilleTown.NAME);
    }
}
