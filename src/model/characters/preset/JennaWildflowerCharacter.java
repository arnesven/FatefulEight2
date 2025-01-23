package model.characters.preset;

import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.ArdhCastle;

import static model.classes.Classes.*;
import static model.classes.Classes.ART;
import static model.races.Race.SOUTHERN_HUMAN;

public class JennaWildflowerCharacter extends GameCharacter {
    public JennaWildflowerCharacter() {
        super("Jenna", "Wildflower", SOUTHERN_HUMAN, THF,
                new JennaWildflower(), new CharacterClass[]{THF, WIZ, SPY, ART});
        addToPersonality(PersonalityTrait.generous);
        addToPersonality(PersonalityTrait.brave);
        addToPersonality(PersonalityTrait.mischievous);
        setHomeTown(ArdhCastle.CASTLE_NAME);
    }
}
