package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.LittleErindeTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.MAR;
import static model.races.Race.HALF_ORC;

public class MordKroftCharacter extends PresetCharacter {
    public MordKroftCharacter() {
        super("Mord", "Kroft", HALF_ORC, PAL,
                new MordKroft(), new CharacterClass[]{PAL, BBN, CAP, MAR},
                LittleErindeTown.NAME, List.of(PersonalityTrait.jovial, PersonalityTrait.romantic, PersonalityTrait.gluttonous));
    }
}
