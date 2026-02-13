package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.LowerThelnTown;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.THF;
import static model.races.Race.DARK_ELF;

public class MialeeSeverinCharacter extends PresetCharacter {
    public MialeeSeverinCharacter() {
        super("Mialee", "Severin", DARK_ELF, WIT,
                new MialeeSeverin(), new CharacterClass[]{WIT, SOR, SPY, THF},
                LowerThelnTown.NAME, List.of(PersonalityTrait.irritable, PersonalityTrait.anxious, PersonalityTrait.unkind));
    }
}
