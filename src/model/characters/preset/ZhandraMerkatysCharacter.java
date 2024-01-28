package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import static model.classes.Classes.*;
import static model.classes.Classes.MIN;
import static model.races.Race.SOUTHERN_HUMAN;

public class ZhandraMerkatysCharacter extends model.characters.GameCharacter {
    public ZhandraMerkatysCharacter() {
        super("Zhandra", "Merkatys", SOUTHERN_HUMAN, WIT,
                new ZhandraMerkatys(), new CharacterClass[]{WIT, BBN, MAR, MIN});
        addToPersonality(PersonalityTrait.rude);
        addToPersonality(PersonalityTrait.romantic);
        addToPersonality(PersonalityTrait.critical);
    }
}
