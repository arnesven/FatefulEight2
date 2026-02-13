package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.LittleErindeTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.MAR;
import static model.races.Race.NORTHERN_HUMAN;

public class EmilyFourhornCharacter extends PresetCharacter {
    public EmilyFourhornCharacter() {
        super("Emily", "Fourhorn", NORTHERN_HUMAN, AMZ,
                new EmilyFourhorn(), new CharacterClass[]{AMZ, BRD, SOR, MAR},
                LittleErindeTown.NAME, List.of(PersonalityTrait.irritable, PersonalityTrait.intellectual, PersonalityTrait.playful));
    }
}
