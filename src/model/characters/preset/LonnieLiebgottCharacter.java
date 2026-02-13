package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.UrnTownTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.THF;
import static model.races.Race.NORTHERN_HUMAN;

public class LonnieLiebgottCharacter extends PresetCharacter {
    public LonnieLiebgottCharacter() {
        super("Lonnie", "Liebgott", NORTHERN_HUMAN, MIN,
                new LonnieLiebgott(), new CharacterClass[]{NOB, MIN, MAG, THF},
                UrnTownTown.NAME, List.of(PersonalityTrait.greedy, PersonalityTrait.playful, PersonalityTrait.mischievous));
    }
}
