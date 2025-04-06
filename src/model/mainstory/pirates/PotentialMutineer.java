package model.mainstory.pirates;

import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.characters.appearance.CharacterAppearance;
import model.items.weapons.Pistol;
import model.races.Race;

import java.io.Serializable;
import java.util.List;

public class PotentialMutineer implements Serializable {
    public static final List<PersonalityTrait> PERSONALITIES = List.of(
            PersonalityTrait.jovial, PersonalityTrait.aggressive,
            PersonalityTrait.friendly, PersonalityTrait.unkind);
    private final boolean isFlippedWeapon;
    private GameCharacter character;
    private PersonalityTrait personality;
    private boolean isTrans;
    private boolean likesRum;
    private boolean flippedWeapon;

    public PotentialMutineer(GameCharacter chara, PersonalityTrait personality, boolean trans, boolean likesRum, boolean flippedWeapon) {
        this.character = chara;
        this.personality = personality;
        this.isTrans = trans;
        this.likesRum = likesRum;
        this.isFlippedWeapon = flippedWeapon;
    }

    public GameCharacter getCharacter() {
        return character;
    }

    public String getName() {
        return character.getFirstName();
    }

    public CharacterAppearance getAppearance() {
        return character.getAppearance();
    }

    public boolean getGender() {
        return character.getGender();
    }

    public boolean isAggressive() {
        return this.personality == PersonalityTrait.aggressive;
    }

    public boolean isUnkind() {
        return this.personality == PersonalityTrait.unkind;
    }

    public boolean isJovial() {
        return this.personality == PersonalityTrait.jovial;
    }

    public boolean isFriendly() {
        return this.personality == PersonalityTrait.friendly;
    }

    public Race getRace() {
        return character.getRace();
    }

    public boolean isTrans() {
        return isTrans;
    }

    public boolean isFlippedWeapon() {
        return isFlippedWeapon;
    }

    public boolean likesRum() {
        return likesRum;
    }

    public boolean usesPistol() {
        return character.getEquipment().getWeapon() instanceof Pistol;
    }
}
