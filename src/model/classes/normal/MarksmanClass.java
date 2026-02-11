package model.classes.normal;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.BowInHandDetail;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Looks;
import model.classes.Skill;
import model.classes.WeightedSkill;
import model.items.Equipment;
import model.items.Item;
import model.items.clothing.LeatherTunic;
import model.items.weapons.LightCrossbow;
import model.items.weapons.LongBow;
import model.items.weapons.TrainingBow;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.FaceAndClothesSpriteWithBack;

import java.util.List;

public class MarksmanClass extends CharacterClass {
    private static final MyColors ARMOR_COLOR = MyColors.BROWN;
    private final BowInHandDetail bow;

    public MarksmanClass() {
        super("Marksman", "MAR", 8, 6, false, 14,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Academics, 3),
                        new WeightedSkill(Skill.Blades, 3),
                        new WeightedSkill(Skill.Bows, 5),
                        new WeightedSkill(Skill.Leadership, 2),
                        new WeightedSkillPlus(Skill.Logic, 1),
                        new WeightedSkill(Skill.Perception, 5),
                        new WeightedSkill(Skill.Search, 4),
                        new WeightedSkill(Skill.Sneak, 4),
                        new WeightedSkill(Skill.SeekInfo, 2),
                        new WeightedSkill(Skill.Survival, 3)
                });
        this.bow = new BowInHandDetail();
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnTunic(characterAppearance, MyColors.DARK_GRAY);
        Looks.putOnLightArmor(characterAppearance, ARMOR_COLOR, MyColors.DARK_GRAY);
        putOnLeatherCap(characterAppearance, ARMOR_COLOR);
        if (characterAppearance instanceof AdvancedAppearance) {
            bow.applyYourself((AdvancedAppearance) characterAppearance,
                    characterAppearance.getRace());
        }
    }

    private void putOnLeatherCap(CharacterAppearance characterAppearance, MyColors color) {
        characterAppearance.removeOuterHair();
        for (int y = 1; y <= 2; ++y) {
            for (int x = 2; x <= 4; ++x) {
                characterAppearance.setSprite(x, y, new FaceAndClothesSpriteWithBack(0xAB + 0x10 * y + x, characterAppearance.getHairColor(), color));
            }
        }
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x120, ARMOR_COLOR, race.getColor(), appearance.getBackHairOnly(), appearance.getHalfBackHair());
    }

    @Override
    public Equipment getDefaultEquipment() {
        return new Equipment(new TrainingBow());
    }

    @Override
    public boolean isBackRowCombatant() {
        return true;
    }

    @Override
    protected MyColors getIconColor() {
        return MyColors.PEACH;
    }

    @Override
    protected int getIconNumber() {
        return 0x11;
    }

    @Override
    public String getHowToLearn() {
        return "Archers at castles will probably be able to teach you how to become a marksman. Wood elves are sometimes marksmen. " +
                "Elves can be found in woods and plains. There are also many veterans of wars " +
                "scattered about. Look for them in farmlands, they can often instruct you on the ways of being a soldier.";
    }

    @Override
    public String getDescription() {
        return "Marksmen are good archers and scouts. They have good survival skills, and are generally perceptive and stealthy." +
                "Their basic combat training lets them have a basic proficiency of weapon types apart from the bow.";
    }

    @Override
    public List<Item> getStartingItems() {
        return List.of(new LongBow(), new LightCrossbow(), new LeatherTunic());
    }
}
