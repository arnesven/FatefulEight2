package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.Equipment;
import model.items.weapons.WoodenSpear;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.Sprite8x8;

public class AmazonClass extends CharacterClass {
    protected AmazonClass() {
        super("Amazon", "AMZ", 7, 7, false, 22,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Acrobatics, 4),
                        new WeightedSkill(Skill.Bows, 4),
                        new WeightedSkill(Skill.Endurance, 3),
                        new WeightedSkill(Skill.MagicGreen, 2),
                        new WeightedSkill(Skill.Perception, 4),
                        new WeightedSkill(Skill.Polearms, 4),
                        new WeightedSkill(Skill.Search, 3),
                        new WeightedSkill(Skill.Sneak, 4),
                        new WeightedSkill(Skill.Survival, 5)
                });
    }

    @Override
    public String getDescription() {
        return "Amazons are native warriors from forests, jungles and swamplands. " +
                "They are light fighters, often skilled in " +
                "more primitive forms of weapons. Amazons are light on their feet, " +
                "brilliant trail blazers, excellent survivalists " +
                "and possess a small affinity for green magic.";
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnHideRight(characterAppearance, MyColors.BROWN);
        Looks.putOnHideLeft(characterAppearance, MyColors.BROWN);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x17, MyColors.BROWN, appearance.getNormalHair());
    }

    @Override
    public void finalizeLook(CharacterAppearance appearance) {
        Looks.putOnNecklace(appearance);
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
    protected int getIconNumber() {
        return 0;
    }

    @Override
    protected MyColors getIconColor() {
        return MyColors.GREEN;
    }

    @Override
    public String getHowToLearn() {
        return "Sometimes you can encounter amazons in swamps who will teach you how to become one.";
    }
}
