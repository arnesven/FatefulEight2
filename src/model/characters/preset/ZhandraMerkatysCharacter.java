package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.LowerThelnTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.MIN;
import static model.races.Race.SOUTHERN_HUMAN;

public class ZhandraMerkatysCharacter extends PresetCharacter {
    public ZhandraMerkatysCharacter() {
        super("Zhandra", "Merkatys", SOUTHERN_HUMAN, WIT,
                new ZhandraMerkatys(), new CharacterClass[]{WIT, BBN, MAR, MIN},
                LowerThelnTown.NAME, List.of(PersonalityTrait.rude, PersonalityTrait.romantic, PersonalityTrait.critical));
    }
}
