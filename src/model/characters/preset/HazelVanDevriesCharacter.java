package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.SunblazeCastle;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.THF;
import static model.races.Race.SOUTHERN_HUMAN;

public class HazelVanDevriesCharacter extends PresetCharacter {
    public HazelVanDevriesCharacter() {
        super("Hazel", "Van Devries", SOUTHERN_HUMAN, NOB,
                new HazelVanDevries(), new CharacterClass[]{NOB, ASN, MAG, THF},
                SunblazeCastle.CASTLE_NAME, List.of(PersonalityTrait.rude, PersonalityTrait.brave, PersonalityTrait.romantic));
    }
}
