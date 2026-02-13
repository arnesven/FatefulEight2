package model.characters.preset;

import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.RoukonTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.PAL;
import static model.races.Race.HALFLING;

public class BungoDarkwoodCharacter extends PresetCharacter {
    public BungoDarkwoodCharacter() {
        super("Bungo", "Darkwood", HALFLING, BBN,
                new BungoDarkwood(), new CharacterClass[]{BBN, CAP, NOB, PAL},
                RoukonTown.NAME, List.of(PersonalityTrait.snobby, PersonalityTrait.diplomatic, PersonalityTrait.critical));
    }
}
