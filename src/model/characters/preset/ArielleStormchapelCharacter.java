package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.EastDurhamTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.PAL;
import static model.races.Race.NORTHERN_HUMAN;

public class ArielleStormchapelCharacter extends PresetCharacter {
    public ArielleStormchapelCharacter() {
        super("Arielle", "Stormchapel", NORTHERN_HUMAN, THF,
                new ArielleStormchapel(), new CharacterClass[]{THF, ASN, BBN, PAL},
                EastDurhamTown.NAME, List.of(PersonalityTrait.snobby, PersonalityTrait.jovial, PersonalityTrait.romantic));
    }
}
