package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;

import static model.classes.Classes.*;
import static model.classes.Classes.SOR;
import static model.races.Race.DARK_ELF;

public class MuldanEbonclawCharacter extends model.characters.GameCharacter {
    public MuldanEbonclawCharacter() {
        super("Muldan", "Ebonclaw", DARK_ELF, PRI,
                new MuldanEbonclaw(), new CharacterClass[]{PRI, NOB, BBN, SOR});
        addToPersonality(PersonalityTrait.stingy);
        addToPersonality(PersonalityTrait.diplomatic);
    }
}
