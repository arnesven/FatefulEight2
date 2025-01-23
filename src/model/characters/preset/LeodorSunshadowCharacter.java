package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.EastDurhamTown;

import static model.classes.Classes.*;
import static model.classes.Classes.WIZ;
import static model.races.Race.DARK_ELF;

public class LeodorSunshadowCharacter extends model.characters.GameCharacter {
    public LeodorSunshadowCharacter() {
        super("Leodor", "Sunshadow", DARK_ELF, NOB,
                new LeodorSunshadow(), new CharacterClass[]{NOB, BKN, BRD, WIZ});
        addToPersonality(PersonalityTrait.unkind);
        addToPersonality(PersonalityTrait.cold);
        addToPersonality(PersonalityTrait.intellectual);
        setHomeTown(EastDurhamTown.NAME);
    }
}
