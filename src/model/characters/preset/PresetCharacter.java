package model.characters.preset;

import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Classes;
import model.combat.conditions.Condition;
import model.races.Race;
import util.MyRandom;

import java.util.Arrays;
import java.util.List;

public class PresetCharacter extends GameCharacter {
    private final String lastName;
    private final String homeTown;
    private final List<PersonalityTrait> traits;
    private CharacterClass[] classes;
    public PresetCharacter(String firstName, String lastName, Race race, CharacterClass charClass,
                           CharacterAppearance appearance, CharacterClass[] classes,
                           String homeTown, List<PersonalityTrait> traits) {
        super(firstName, lastName, race, charClass, appearance);
        this.lastName = lastName;
        this.homeTown = homeTown;
        this.classes = classes;
        this.traits = traits;
        setHomeTown(homeTown);
        for (PersonalityTrait trait : traits) {
            addToPersonality(trait);
        }
    }

    public CharacterClass[] getClasses() {
        return classes;
    }

    public void setClasses(CharacterClass[] classes) {
        this.classes = classes;
    }

    @Override
    public GameCharacter copy() {
        PresetCharacter pc = new PresetCharacter(getFirstName(), lastName, getRace(), getCharClass(), getAppearance().copy(),
                classes, homeTown, traits);
        pc.setLevel(getLevel());
        for (Condition cond : getConditions()) {
            pc.addCondition(cond);
        }
        return pc;
    }

    public void setRandomStartingClass() {
        setClass(MyRandom.sample(Arrays.asList(classes)));
        setEquipment(getCharClass().getDefaultEquipment());
        super.setCurrentHp(getMaxHP());
    }
}
