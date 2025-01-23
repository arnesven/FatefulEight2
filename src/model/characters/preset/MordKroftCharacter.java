package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.LittleErindeTown;

import static model.classes.Classes.*;
import static model.classes.Classes.MAR;
import static model.races.Race.HALF_ORC;

public class MordKroftCharacter extends model.characters.GameCharacter {
    public MordKroftCharacter() {
        super("Mord", "Kroft", HALF_ORC, PAL,
                new MordKroft(), new CharacterClass[]{PAL, BBN, CAP, MAR});
        addToPersonality(PersonalityTrait.jovial);
        addToPersonality(PersonalityTrait.romantic);
        addToPersonality(PersonalityTrait.gluttonous);
        setHomeTown(LittleErindeTown.NAME);
    }
}
