package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.EastDurhamTown;

import static model.classes.Classes.*;
import static model.classes.Classes.PAL;
import static model.races.Race.NORTHERN_HUMAN;

public class ArielleStormchapelCharacter extends model.characters.GameCharacter {
    public ArielleStormchapelCharacter() {
        super("Arielle", "Stormchapel", NORTHERN_HUMAN, THF,
                new ArielleStormchapel(), new CharacterClass[]{THF, ASN, BBN, PAL});
        addToPersonality(PersonalityTrait.snobby);
        addToPersonality(PersonalityTrait.jovial);
        addToPersonality(PersonalityTrait.romantic);
        setHomeTown(EastDurhamTown.NAME);
    }
}
