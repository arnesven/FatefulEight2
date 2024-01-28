package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import static model.classes.Classes.*;
import static model.classes.Classes.MAR;
import static model.races.Race.NORTHERN_HUMAN;

public class EmilyFourhornCharacter extends model.characters.GameCharacter {
    public EmilyFourhornCharacter() {
        super("Emily", "Fourhorn", NORTHERN_HUMAN, AMZ,
                new EmilyFourhorn(), new CharacterClass[]{AMZ, BRD, SOR, MAR});
        addToPersonality(PersonalityTrait.irritable);
    }
}
