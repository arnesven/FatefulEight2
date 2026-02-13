package model.characters.preset;

import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.BogdownCastle;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.SOR;
import static model.races.Race.HALF_ORC;

public class KruskTalandroCharacter extends PresetCharacter {
    public KruskTalandroCharacter() {
        super("Krusk", "Talandro", HALF_ORC, WIT,
                new KruskTalandro(), new CharacterClass[]{WIT, DRU, MAG, SOR},
                BogdownCastle.CASTLE_NAME, List.of(PersonalityTrait.generous, PersonalityTrait.lawful, PersonalityTrait.intellectual));
    }
}
