package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;

import static model.classes.Classes.*;
import static model.classes.Classes.SPY;
import static model.races.Race.WOOD_ELF;

public class LianaClearwaterCharacter extends model.characters.GameCharacter {
    public LianaClearwaterCharacter() {
        super("Liana", "Clearwater", WOOD_ELF, PAL,
                new LianaClearwater(), new CharacterClass[]{PAL, CAP, WIZ, SPY});
        addToPersonality(PersonalityTrait.encouraging);
        addToPersonality(PersonalityTrait.diplomatic);
        addToPersonality(PersonalityTrait.prudish);
    }
}
