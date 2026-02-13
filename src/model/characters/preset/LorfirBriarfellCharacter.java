package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.ArkvaleCastle;

import java.util.List;

import static model.classes.Classes.*;
import static model.classes.Classes.THF;
import static model.races.Race.WOOD_ELF;

public class LorfirBriarfellCharacter extends PresetCharacter {
    public LorfirBriarfellCharacter() {
        super("Lorfir", "Briarfell", WOOD_ELF, ART,
                new LorfirBriarfell(), new CharacterClass[]{ART, AMZ, DRU, THF},
                ArkvaleCastle.CASTLE_NAME, List.of(PersonalityTrait.snobby, PersonalityTrait.cold, PersonalityTrait.calm));
    }
}
