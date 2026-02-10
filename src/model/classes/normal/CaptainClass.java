package model.classes.normal;

import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Looks;
import model.classes.Skill;
import model.classes.WeightedSkill;
import model.items.Equipment;
import model.items.Item;
import model.items.accessories.*;
import model.items.clothing.RustyRingMail;
import model.items.weapons.Broadsword;
import model.items.weapons.WoodenSpear;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.ClothesSpriteWithBack;

import java.util.List;

public class CaptainClass extends CharacterClass {
    private static final MyColors ARMOR_COLOR = MyColors.GRAY;
    private static final MyColors SECONDARY_COLOR = MyColors.DARK_GRAY;

    protected CaptainClass(String className, String shortName) {
        super(className, shortName, 8, 4, true, 14,
                new WeightedSkill[]{
                        new WeightedSkillMinus(Skill.Academics, 2),
                        new WeightedSkill(Skill.Blades, 4),
                        new WeightedSkillMinus(Skill.Bows, 2),
                        new WeightedSkill(Skill.Endurance, 3),
                        new WeightedSkillMinus(Skill.Leadership, 5),
                        new WeightedSkill(Skill.Logic, 2),
                        new WeightedSkill(Skill.Perception, 3),
                        new WeightedSkillMinus(Skill.Polearms, 3),
                        new WeightedSkill(Skill.Search, 3),
                        new WeightedSkillMinus(Skill.Security, 2),
                        new WeightedSkill(Skill.Survival, 3)
                });
    }

    public CaptainClass() {
        this("Captain", "C");
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
        return new AvatarSprite(race, 0x120, ARMOR_COLOR, race.getColor(),
                appearance.getBackHairOnly(), appearance.getHalfBackHair());
    }


    @Override
    public Equipment getDefaultEquipment() {
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
    public String getHowToLearn() {
        return "A master-at-arms at a castle could show you how to become a captain. There are also many veterans of wars " +
                "scattered about. Look for them in farmlands, they can often instruct you on the ways of being a soldier.";
    }

    @Override
    public String getDescription() {
        return "Captains are capable men-at-arms, weathered by life in the army. They are proficient with a variety of " +
                "weapons and are good leaders. Captains are often trained strategists and exhibit fair survival skills.";
    }

    @Override
    public List<Item> getStartingItems() {
        return List.of(new Broadsword(), new RustyRingMail(), new Helm());
    }
}
