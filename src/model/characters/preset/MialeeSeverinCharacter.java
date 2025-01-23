package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.LowerThelnTown;

import static model.classes.Classes.*;
import static model.classes.Classes.THF;
import static model.races.Race.DARK_ELF;

public class MialeeSeverinCharacter extends model.characters.GameCharacter {
    public MialeeSeverinCharacter() {
        super("Mialee", "Severin", DARK_ELF, WIT,
                new MialeeSeverin(), new CharacterClass[]{WIT, SOR, SPY, THF});
        addToPersonality(PersonalityTrait.irritable);
        addToPersonality(PersonalityTrait.anxious);
        addToPersonality(PersonalityTrait.unkind);
        setHomeTown(LowerThelnTown.NAME);
    }
}
