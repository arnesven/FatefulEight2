package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;

import static model.classes.Classes.*;
import static model.classes.Classes.THF;
import static model.races.Race.SOUTHERN_HUMAN;

public class HazelVanDevriesCharacter extends model.characters.GameCharacter {
    public HazelVanDevriesCharacter() {
        super("Hazel", "Van Devries", SOUTHERN_HUMAN, NOB,
                new HazelVanDevries(), new CharacterClass[]{NOB, ASN, MAG, THF});
        addToPersonality(PersonalityTrait.rude);
        addToPersonality(PersonalityTrait.mischievous);
        addToPersonality(PersonalityTrait.romantic);
    }
}
