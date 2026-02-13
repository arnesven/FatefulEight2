package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.SouthMeadhomeTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.WIT;
import static model.races.Race.NORTHERN_HUMAN;

public class RolfFrytCharacter extends PresetCharacter {
    public RolfFrytCharacter() {
        super("Rolf", "Fryt", NORTHERN_HUMAN, PRI,
                new RolfFryt(), new CharacterClass[]{PRI, MIN, SPY, WIT},
                SouthMeadhomeTown.NAME, List.of(PersonalityTrait.unkind, PersonalityTrait.gluttonous, PersonalityTrait.snobby));
    }
}
