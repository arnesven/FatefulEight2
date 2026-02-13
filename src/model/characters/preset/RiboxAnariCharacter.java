package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.SunblazeCastle;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.NOB;
import static model.races.Race.WOOD_ELF;

public class RiboxAnariCharacter extends PresetCharacter {
    public RiboxAnariCharacter() {
        super("Ribox", "Anari", WOOD_ELF, MIN,
                new RiboxAnari(), new CharacterClass[]{MIN, BBN, PRI, NOB},
                SunblazeCastle.CASTLE_NAME, List.of(PersonalityTrait.irritable, PersonalityTrait.aggressive, PersonalityTrait.friendly));
    }
}
