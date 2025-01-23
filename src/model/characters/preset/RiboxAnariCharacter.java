package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.SunblazeCastle;

import static model.classes.Classes.*;
import static model.classes.Classes.NOB;
import static model.races.Race.WOOD_ELF;

public class RiboxAnariCharacter extends model.characters.GameCharacter {
    public RiboxAnariCharacter() {
        super("Ribox", "Anari", WOOD_ELF, MIN,
                new RiboxAnari(), new CharacterClass[]{MIN, BBN, PRI, NOB});
        addToPersonality(PersonalityTrait.irritable);
        addToPersonality(PersonalityTrait.aggressive);
        addToPersonality(PersonalityTrait.friendly);
        setHomeTown(SunblazeCastle.CASTLE_NAME);
    }
}
