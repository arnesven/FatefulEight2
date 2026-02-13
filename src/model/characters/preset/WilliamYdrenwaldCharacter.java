package model.characters.preset;

import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.SaintQuellinTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.SOR;
import static model.races.Race.NORTHERN_HUMAN;

public class WilliamYdrenwaldCharacter extends PresetCharacter {
    public WilliamYdrenwaldCharacter() {
        super("William", "Ydrenwald", NORTHERN_HUMAN, WIZ,
                new WilliamYdrenwald(), new CharacterClass[]{WIZ, DRU, MAG, SOR},
                SaintQuellinTown.NAME, List.of(PersonalityTrait.encouraging, PersonalityTrait.intellectual, PersonalityTrait.benevolent));
    }
}
