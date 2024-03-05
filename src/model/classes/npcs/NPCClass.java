package model.classes.npcs;

import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Skill;
import model.items.Equipment;
import model.items.Item;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

import java.util.ArrayList;
import java.util.List;

public abstract class NPCClass extends CharacterClass {
    protected NPCClass(String name) {
        super(name, "", 7, 0, false, 0,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Perception, 1),
                        new WeightedSkill(Skill.Blades, 1)});
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

    @Override
    protected int getIconNumber() {
        return 0;
    }

    @Override
    protected MyColors getIconColor() {
        return MyColors.BEIGE;
    }

    @Override
    public String getDescription() {
        return "Unused";
    }

    @Override
    public String getHowToLearn() {
        return "Undefined";
    }

    @Override
    public List<Item> getStartingItems() {
        return new ArrayList<>();
    }
}
