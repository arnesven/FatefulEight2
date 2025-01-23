package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.ArkvaleCastle;

import static model.classes.Classes.*;
import static model.classes.Classes.THF;
import static model.races.Race.WOOD_ELF;

public class LorfirBriarfellCharacter extends model.characters.GameCharacter {
    public LorfirBriarfellCharacter() {
        super("Lorfir", "Briarfell", WOOD_ELF, ART,
                new LorfirBriarfell(), new CharacterClass[]{ART, AMZ, DRU, THF});
        addToPersonality(PersonalityTrait.snobby);
        addToPersonality(PersonalityTrait.cold);
        addToPersonality(PersonalityTrait.calm);
        setHomeTown(ArkvaleCastle.CASTLE_NAME);
    }
}
