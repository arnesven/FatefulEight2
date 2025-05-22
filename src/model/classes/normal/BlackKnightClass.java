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
import model.races.Race;
import view.MyColors;
import view.sprites.*;

import java.util.List;

public class BlackKnightClass extends CharacterClass {
    public BlackKnightClass() {
        super("Black Knight", "BKN", 8, 6, true, 16,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Blades, 3),
                        new WeightedSkill(Skill.BluntWeapons, 5),
                        new WeightedSkill(Skill.Endurance, 2),
                        new WeightedSkill(Skill.Labor, 2),
                        new WeightedSkill(Skill.Leadership, 2),
                        new WeightedSkill(Skill.Perception, 4),
                        new WeightedSkill(Skill.Persuade, 2),
                        new WeightedSkill(Skill.Search, 1),
                        new WeightedSkill(Skill.Survival, 2)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnArmor(characterAppearance, MyColors.DARK_GRAY, MyColors.DARK_GREEN);
        putOnKnightsHelm(characterAppearance, MyColors.DARK_GRAY, MyColors.DARK_GREEN);
    }


    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x80, MyColors.DARK_GRAY, race.getColor(), CharacterAppearance.noHair(), CharacterAppearance.noHair());
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
        for (int y = 1; y <= 4; ++y) {
            characterAppearance.setSprite(1, y, new FilledBlockSprite(MyColors.BLACK));
            characterAppearance.setSprite(5, y, new FilledBlockSprite(MyColors.BLACK));
        }
        for (int y = 0; y <= 5; ++y) {
            for (int x = 2; x <= 4; ++x) {
                if (y == 4) {
                    characterAppearance.setSprite(x, y, new ClothesSpriteWithBack(0x20 + x + 2, areaColor, lineColor));
                } else {
                    characterAppearance.setSprite(x, y, new ClothesSpriteWithBack(0x10 * y + x + 2, areaColor, lineColor));
                }
            }
        }
    }

    @Override
    public void finalizeLook(CharacterAppearance appearance) {
        // remove ears
        appearance.setSprite(1, 3, new FilledBlockSprite(MyColors.BLACK));
        appearance.setSprite(5, 3, new FilledBlockSprite(MyColors.BLACK));
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
        return List.of(new Mace(), new RustyChestPlate(), new HeraldicShield());
    }
}
