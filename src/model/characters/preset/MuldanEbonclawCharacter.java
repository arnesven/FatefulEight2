package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.AckervilleTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.SOR;
import static model.races.Race.DARK_ELF;

public class MuldanEbonclawCharacter extends PresetCharacter {
    public MuldanEbonclawCharacter() {
        super("Muldan", "Ebonclaw", DARK_ELF, PRI,
                new MuldanEbonclaw(), new CharacterClass[]{PRI, NOB, BBN, SOR},
                AckervilleTown.NAME, List.of(PersonalityTrait.stingy, PersonalityTrait.diplomatic, PersonalityTrait.forgiving));
    }
}
