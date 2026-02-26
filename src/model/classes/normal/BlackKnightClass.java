package model.classes.normal;

import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Looks;
import model.classes.Skill;
import model.classes.WeightedSkill;
import model.items.Item;
import model.items.accessories.HeraldicShield;
import model.items.clothing.RustyChestPlate;
import model.items.weapons.Club;
import model.items.Equipment;
import model.items.weapons.Mace;
import model.items.weapons.Scepter;
import model.items.weapons.UnarmedCombatWeapon;
import model.races.Race;
import util.MyTriplet;
import view.MyColors;
import view.sprites.*;

import java.util.List;

public class BlackKnightClass extends CharacterClass {
    public BlackKnightClass() {
        super("Black Knight", "BKN", 8, 6, true, 16,
                new WeightedSkill[]{
                        new WeightedSkillPlus(Skill.Blades, 3),
                        new WeightedSkill(Skill.BluntWeapons, 5),
                        new WeightedSkill(Skill.Endurance, 2),
                        new WeightedSkill(Skill.Labor, 2),
                        new WeightedSkill(Skill.Leadership, 2),
                        new WeightedSkill(Skill.Perception, 3),
                        new WeightedSkillPlus(Skill.Persuade, 2),
                        new WeightedSkill(Skill.Search, 1),
                        new WeightedSkillPlus(Skill.Survival, 2),
                        new WeightedSkillMinus(Skill.UnarmedCombat, 3)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnArmor(characterAppearance, MyColors.DARK_GRAY, MyColors.DARK_GREEN);
        putOnKnightsHelm(characterAppearance, MyColors.DARK_GRAY, MyColors.DARK_GREEN);
    }


    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, AvatarSprite.ARMORED_BASE, MyColors.DARK_GRAY, race.getColor(), MyColors.ORANGE, CharacterAppearance.noHair(), CharacterAppearance.noHair(),
                makeAvatarHelm(appearance));
    }

    private MyTriplet<Sprite, Sprite, Sprite> makeAvatarHelm(CharacterAppearance appearance) {
        return AvatarSprite.makeHat(appearance, "blackknighthelm", 0x08,
                MyColors.BLACK, MyColors.DARK_GRAY, MyColors.ORANGE, MyColors.DARK_GRAY);
    }

    @Override
    public Equipment getDefaultEquipment() {
        return new Equipment(new Club());
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }

    public static void putOnKnightsHelm(CharacterAppearance characterAppearance, MyColors areaColor, MyColors lineColor) {
        characterAppearance.removeOuterHair();
        for (int y = 2; y <= 4; ++y) {
            characterAppearance.setSprite(1, y, new ClothesSpriteWithBack(0x1A6 + (y-2)*0x10, areaColor, lineColor));
            characterAppearance.setSprite(5, y, new ClothesSpriteWithBack(0x1A7 + (y-2)*0x10, areaColor, lineColor));
        }
        for (int y = 0; y <= 5; ++y) {
            for (int x = 2; x <= 4; ++x) {
                characterAppearance.setSprite(x, y, new ClothesSpriteWithBack(0x10 * y + x + 2, areaColor, lineColor));
            }
        }
    }

    @Override
    public boolean showFacialHair() {
        return false;
    }

    @Override
    public boolean showDetail() {
        return false;
    }

    @Override
    public boolean showHairInBack() {
        return false;
    }

    @Override
    public boolean coversEars() {
        return true;
    }

    @Override
    protected MyColors getIconColor() {
        return MyColors.DARK_GREEN;
    }

    @Override
    protected int getIconNumber() {
        return 0x05;
    }

    @Override
    public String getHowToLearn() {
        return "Black Knights sometimes guard river crossings, although I doubt one would agree to teach you their " +
                "martial ways, unless you could prove yourself worthy of course.";
    }

    @Override
    public String getDescription() {
        return "Not all knights are graceful heroes. Some betray their masters, are dishonorably discharged, or simply " +
                "find their own selfish paths. Black Knights are fierce fighters who manage to stay agile even in heavy armor. " +
                "What they lack in social skills they make up for in battle prowess and keen senses.";
    }

    @Override
    public List<Item> getStartingItems() {
        return List.of(new Scepter(), new RustyChestPlate(), new HeraldicShield());
    }

    @Override
    public boolean coversEyebrows() {
        return true;
    }
}
