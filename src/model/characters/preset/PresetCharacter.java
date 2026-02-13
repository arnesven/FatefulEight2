package model.characters.preset;

import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.races.Race;

import java.util.List;

public class PresetCharacter extends GameCharacter {
    private final CharacterClass[] classes;
    public PresetCharacter(String firstName, String lastName, Race race, CharacterClass charClass,
                           CharacterAppearance appearance, CharacterClass[] classes,
                           String homeTown, List<PersonalityTrait> traits) {
        super(firstName, lastName, race, charClass, appearance, classes);
        this.classes = classes;
        setHomeTown(homeTown);
        for (PersonalityTrait trait : traits) {
            addToPersonality(trait);
        }

    }
}
