package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.Equipment;
import model.items.accessories.SkullCap;
import model.items.clothing.LeatherArmor;
import model.items.weapons.Glaive;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class GoblinClass extends CharacterClass {
    protected GoblinClass() {
        super("Goblin", "GBN", 5, 5, true, 0,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Axes, 4),
                        new WeightedSkill(Skill.Bows, 4),
                        new WeightedSkill(Skill.BluntWeapons, 4),
                        new WeightedSkill(Skill.MagicBlack, 5),
                        new WeightedSkill(Skill.MagicRed, 5),
                        new WeightedSkill(Skill.MagicBlue, 5),
                        new WeightedSkill(Skill.Polearms, 5),
                        new WeightedSkill(Skill.Search, 3),
                        new WeightedSkill(Skill.Security, 4),
                        new WeightedSkill(Skill.Sneak, 5)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {

    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x97, MyColors.BEIGE, appearance.getNormalHair());
    }

    @Override
    public Equipment getStartingEquipment() {
        return new Equipment(new Glaive(), new LeatherArmor(), new SkullCap());
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }
}