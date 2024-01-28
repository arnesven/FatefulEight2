package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;

import static model.classes.Classes.*;
import static model.classes.Classes.PRI;
import static model.races.Race.SOUTHERN_HUMAN;

public class JordynStrongCharacter extends model.characters.GameCharacter {
    public JordynStrongCharacter() {
        super("Jordyn", "Strong", SOUTHERN_HUMAN, AMZ,
                new JordynStrong(), new CharacterClass[]{AMZ, BRD, FOR, PRI});
        addToPersonality(PersonalityTrait.benevolent);
    }
}
