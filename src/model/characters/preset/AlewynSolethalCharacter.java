package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.BogdownCastle;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.MAG;
import static model.races.Race.HIGH_ELF;

public class AlewynSolethalCharacter extends PresetCharacter {
    public AlewynSolethalCharacter() {
        super("Alewyn", "Solethal", HIGH_ELF, ART,
                new AlewynSolethal(), new CharacterClass[]{ART, WIT, AMZ, MAG},
                BogdownCastle.CASTLE_NAME,
                List.of(PersonalityTrait.jovial, PersonalityTrait.diplomatic, PersonalityTrait.greedy));
    }
}
