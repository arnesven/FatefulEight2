package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.LowerThelnTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.NOB;
import static model.races.Race.NORTHERN_HUMAN;

public class DeniseBoydCharacter extends PresetCharacter {
    public DeniseBoydCharacter() {
        super("Denise", "Boyd", NORTHERN_HUMAN, ART,
                new DeniseBoyd(), new CharacterClass[]{ART, BKN, CAP, NOB},
                LowerThelnTown.NAME, List.of(PersonalityTrait.prudish, PersonalityTrait.playful, PersonalityTrait.friendly));
    }
}
