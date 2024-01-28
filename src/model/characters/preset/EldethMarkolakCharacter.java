package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;

import static model.classes.Classes.*;
import static model.classes.Classes.SOR;
import static model.races.Race.DWARF;

public class EldethMarkolakCharacter extends model.characters.GameCharacter {
    public EldethMarkolakCharacter() {
        super("Eldeth", "Markolak", DWARF, PRI,
                new EldethMarkolak(), new CharacterClass[]{PRI, BBN, FOR, SOR});
        addToPersonality(PersonalityTrait.prude);
        addToPersonality(PersonalityTrait.gluttonous);
        addToPersonality(PersonalityTrait.calm);
    }
}
