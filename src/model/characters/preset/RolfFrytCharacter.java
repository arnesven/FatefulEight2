package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;

import static model.classes.Classes.*;
import static model.classes.Classes.WIT;
import static model.races.Race.NORTHERN_HUMAN;

public class RolfFrytCharacter extends model.characters.GameCharacter {
    public RolfFrytCharacter() {
        super("Rolf", "Fryt", NORTHERN_HUMAN, PRI,
                new RolfFryt(), new CharacterClass[]{PRI, MIN, SPY, WIT});
        addToPersonality(PersonalityTrait.unkind);
        addToPersonality(PersonalityTrait.gluttonous);
        addToPersonality(PersonalityTrait.snobby);
    }
}
