package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;

import static model.classes.Classes.*;
import static model.classes.Classes.MIN;
import static model.races.Race.HALF_ORC;

public class PuyetGranthamCharacter extends model.characters.GameCharacter {
    public PuyetGranthamCharacter() {
        super("Puyet", "Grantham", HALF_ORC, ART,
                new PuyetGrantham(), new CharacterClass[]{ART, ASN, FOR, MIN});
        addToPersonality(PersonalityTrait.stingy);
        addToPersonality(PersonalityTrait.greedy);
        addToPersonality(PersonalityTrait.friendly);
    }
}
