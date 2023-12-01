package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.Equipment;
import model.items.weapons.Hatchet;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class ForesterClass extends CharacterClass {
    protected ForesterClass() {
        super("Forester", "F", 10, 4, false, 0,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Acrobatics, 2),
                        new WeightedSkill(Skill.Axes, 5),
                        new WeightedSkill(Skill.BluntWeapons, 1),
                        new WeightedSkill(Skill.Bows, 3),
                        new WeightedSkill(Skill.Endurance, 5),
                        new WeightedSkill(Skill.Entertain, 1),
                        new WeightedSkill(Skill.Labor, 5),
                        new WeightedSkill(Skill.Leadership, 4),
                        new WeightedSkill(Skill.Perception, 2),
                        new WeightedSkill(Skill.Search, 4),
                        new WeightedSkill(Skill.Survival, 5)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnLooseShirt(characterAppearance, MyColors.RED);
        Looks.putOnCap(characterAppearance, MyColors.DARK_GREEN);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0xD0, MyColors.RED, MyColors.DARK_GREEN, appearance.getBackHairOnly());
    }

    @Override
    public Equipment getStartingEquipment() {
        return new Equipment(new Hatchet());
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }

    @Override
    public void finalizeLook(CharacterAppearance appearance) {
        Looks.finalizeCap(appearance);
        Looks.putOnSuspenders(appearance, MyColors.RED, MyColors.BLACK);
    }

    @Override
    protected MyColors getIconColor() {
        return MyColors.GREEN;
    }

    @Override
    protected int getIconNumber() {
        return 0x04;
    }

    @Override
    public String getHowToLearn() {
        return "Look for lumber mills in the forest. There you will find foresters who can teach you their trade. " +
                "I also hear many half-orcs are foresters. Half-orcs dwell in hilly areas.";
    }

    @Override
    public String getDescription() {
        return "Foresters are the wardens of the forest. They are lumberjacks or rangers who feel most at home " +
                "surrounded by tall trees and the musky scent of fir, pine or oak. They are light fighters, but often " +
                "rugged and hardened by hard labor. They are good pathfinders and guids when trekking through the wilderness.";
    }
}
