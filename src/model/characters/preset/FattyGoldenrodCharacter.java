package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;

import static model.classes.Classes.*;
import static model.classes.Classes.SOR;
import static model.races.Race.HALFLING;

public class FattyGoldenrodCharacter extends model.characters.GameCharacter {
    public FattyGoldenrodCharacter() {
        super("Fatty", "Goldenrod", HALFLING, MIN,
                new FattyGoldenrod(), new CharacterClass[]{MIN, BBN, WIT, SOR});
        addToPersonality(PersonalityTrait.unkind);
        addToPersonality(PersonalityTrait.rude);
        addToPersonality(PersonalityTrait.cold);
    }
}
