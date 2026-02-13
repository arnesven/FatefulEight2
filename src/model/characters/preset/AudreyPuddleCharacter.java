package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.UrnTownTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.ART;
import static model.races.Race.HALFLING;

public class AudreyPuddleCharacter extends PresetCharacter {
    public AudreyPuddleCharacter() {
        super("Audrey", "Puddle", HALFLING, SPY,
                new AudreyPuddle(), new CharacterClass[]{BRD, SPY, AMZ, ART},
                UrnTownTown.NAME, List.of(PersonalityTrait.cowardly, PersonalityTrait.playful, PersonalityTrait.encouraging));
    }
}
