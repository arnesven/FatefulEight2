package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.AshtonshireTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.SOR;
import static model.races.Race.DWARF;

public class EldethMarkolakCharacter extends PresetCharacter {
    public EldethMarkolakCharacter() {
        super("Eldeth", "Markolak", DWARF, PRI,
                new EldethMarkolak(), new CharacterClass[]{PRI, BBN, FOR, SOR},
                AshtonshireTown.NAME, List.of(PersonalityTrait.prudish, PersonalityTrait.gluttonous, PersonalityTrait.naive));
    }
}
