package model.characters.preset;

import model.characters.PersonalityTrait;
import model.classes.CharacterClass;
import model.map.locations.CapePaxtonTown;

import static model.classes.Classes.*;
import static model.classes.Classes.SOR;
import static model.races.Race.HIGH_ELF;

public class AtalyaBalefrostCharacter extends model.characters.GameCharacter {
    public AtalyaBalefrostCharacter() {
        super("Atalya", "Balefrost", HIGH_ELF, WIZ,
                new AtalyaBalefrost(), new CharacterClass[]{WIZ, BKN, DRU, SOR});
        addToPersonality(PersonalityTrait.cowardly);
        addToPersonality(PersonalityTrait.naive);
        addToPersonality(PersonalityTrait.stingy);
        setHomeTown(CapePaxtonTown.NAME);
    }
}
