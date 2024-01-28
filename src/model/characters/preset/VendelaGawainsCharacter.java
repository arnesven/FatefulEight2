package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;

import static model.classes.Classes.*;
import static model.classes.Classes.MAG;
import static model.races.Race.HALF_ORC;

public class VendelaGawainsCharacter extends model.characters.GameCharacter {
    public VendelaGawainsCharacter() {
        super("Vendela", "Gawains", HALF_ORC, ASN,
                new VendelaGawains(), new CharacterClass[]{ASN, NOB, WIT, MAG});
        addToPersonality(PersonalityTrait.greedy);
    }
}
