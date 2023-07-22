package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.Equipment;
import model.items.weapons.WoodenSpear;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.ClothesSpriteWithBack;

public class CaptainClass extends CharacterClass {
    private static final MyColors ARMOR_COLOR = MyColors.GRAY;
    private static final MyColors SECONDARY_COLOR = MyColors.DARK_GRAY;

    protected CaptainClass() {
        super("Captain", "C", 8, 5, true, 16,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Blades, 4),
                        new WeightedSkill(Skill.Bows, 2),
                        new WeightedSkill(Skill.Endurance, 3),
                        new WeightedSkill(Skill.Leadership, 5),
                        new WeightedSkill(Skill.Logic, 2),
                        new WeightedSkill(Skill.Perception, 3),
                        new WeightedSkill(Skill.Polearms, 3),
                        new WeightedSkill(Skill.Search, 3),
                        new WeightedSkill(Skill.Security, 2),
                        new WeightedSkill(Skill.Survival, 3)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnTunic(characterAppearance, SECONDARY_COLOR);
        Looks.putOnLightArmor(characterAppearance, ARMOR_COLOR, SECONDARY_COLOR);
        putOnHalfHelm(characterAppearance);
    }

    public static void putOnHalfHelm(CharacterAppearance characterAppearance) {
        characterAppearance.removeOuterHair();
        for (int y = 0; y <= 2; ++y) {
            for (int x = 2; x <= 4; ++x) {
                characterAppearance.setSprite(x, y, new ClothesSpriteWithBack(0x10 * y + x + 5, ARMOR_COLOR, MyColors.DARK_GRAY));
            }
        }
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x07, ARMOR_COLOR, appearance.getBackHairOnly());
    }


    @Override
    public Equipment getStartingEquipment() {
        return new Equipment(new WoodenSpear());
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }

    @Override
    protected MyColors getIconColor() {
        return MyColors.GRAY;
    }

    @Override
    protected int getIconNumber() {
        return 0x13;
    }

    @Override
    public String getDescription() {
        return "Captains are capable men-at-arms, weathered by life in the army. The are proficient with a variety of " +
                "weapons and are good leaders. Captains are often trained strategists and exhibit fair survival skills.";
    }
}
