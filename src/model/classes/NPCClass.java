package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.Equipment;
import model.races.Race;
import view.sprites.AvatarSprite;

public abstract class NPCClass extends CharacterClass {
    protected NPCClass(String name) {
        super(name, "", 0, 0, false, 0, new WeightedSkill[0]);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        throw new IllegalStateException("Should not be used!");
    }

    @Override
    public Equipment getStartingEquipment() {
        return new Equipment();
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }
}