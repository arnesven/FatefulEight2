package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;

import static model.classes.Classes.*;
import static model.races.Race.HALFLING;

public class VzaniAnglerCharacter extends model.characters.GameCharacter {
    public VzaniAnglerCharacter() {
        super("Vzani", "Angler", HALFLING, PRI,
                new VzaniAngler(), new CharacterClass[]{WIZ, MAG, PRI, THF});
        addToPersonality(PersonalityTrait.stingy);
        addToPersonality(PersonalityTrait.calm);
        addToPersonality(PersonalityTrait.critical);
    }
}
