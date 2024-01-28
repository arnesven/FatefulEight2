package model.characters.preset;

import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.CharacterClass;

import static model.classes.Classes.*;
import static model.classes.Classes.PAL;
import static model.races.Race.HALFLING;

public class BungoDarkwoodCharacter extends GameCharacter {
    public BungoDarkwoodCharacter() {
        super("Bungo", "Darkwood", HALFLING, BBN,
                new BungoDarkwood(), new CharacterClass[]{BBN, CAP, NOB, PAL});
        addToPersonality(PersonalityTrait.snobby);
    }
}
