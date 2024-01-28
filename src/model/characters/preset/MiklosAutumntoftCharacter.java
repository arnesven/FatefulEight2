package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;

import static model.classes.Classes.*;
import static model.races.Race.HIGH_ELF;

public class MiklosAutumntoftCharacter extends model.characters.GameCharacter {
    public MiklosAutumntoftCharacter() {
        super("Miklos", "Autumntoft", HIGH_ELF, MAR,
                new MiklosAutumntoft(), new CharacterClass[]{CAP, NOB, PAL, PRI});
        addToPersonality(PersonalityTrait.narcissistic);
        addToPersonality(PersonalityTrait.playful);
        addToPersonality(PersonalityTrait.mischievous);
    }
}
