package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.SheffieldTown;

import static model.classes.Classes.*;
import static model.classes.Classes.DRU;
import static model.races.Race.SOUTHERN_HUMAN;

public class SebastianSmithCharacter extends model.characters.GameCharacter {
    public SebastianSmithCharacter() {
        super("Sebastian", "Smith", SOUTHERN_HUMAN, PAL,
                new SebastianSmith(), new CharacterClass[]{PAL, BKN, CAP, DRU});
        addToPersonality(PersonalityTrait.aggressive);
        addToPersonality(PersonalityTrait.mischievous);
        addToPersonality(PersonalityTrait.intellectual);
        setHomeTown(SheffieldTown.NAME);
    }
}
