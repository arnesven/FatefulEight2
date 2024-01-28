package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;

import static model.classes.Classes.*;
import static model.classes.Classes.NOB;
import static model.races.Race.NORTHERN_HUMAN;

public class DeniseBoydCharacter extends model.characters.GameCharacter {
    public DeniseBoydCharacter() {
        super("Denise", "Boyd", NORTHERN_HUMAN, ART,
                new DeniseBoyd(), new CharacterClass[]{ART, BKN, CAP, NOB});
        addToPersonality(PersonalityTrait.prude);
    }
}
