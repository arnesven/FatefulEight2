package model.classes.normal;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.SpearInHandDetail;
import model.classes.CharacterClass;
import model.classes.Looks;
import model.classes.Skill;
import model.classes.WeightedSkill;
import model.items.Equipment;
import model.items.Item;
import model.items.clothing.PrimitiveArmor;
import model.items.weapons.Javelins;
import model.items.weapons.WoodenSpear;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

import java.util.ArrayList;
import java.util.List;

public class AmazonClass extends CharacterClass {
    private final SpearInHandDetail spear;

    public AmazonClass() {
        super("Amazon", "AMZ", 7, 7, false, 8,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Acrobatics, 4),
                        new WeightedSkillPlus(Skill.Bows, 4),
                        new WeightedSkill(Skill.Endurance, 3),
                        new WeightedSkill(Skill.MagicGreen, 2),
                        new WeightedSkill(Skill.Perception, 4),
                        new WeightedSkill(Skill.Polearms, 4),
                        new WeightedSkill(Skill.Search, 3),
                        new WeightedSkill(Skill.Sneak, 4),
                        new WeightedSkill(Skill.Survival, 5)
                });
        this.spear = new SpearInHandDetail();
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
    public List<Item> getStartingItems() {
        List<Item> starting = new ArrayList<>(List.of(new Javelins(), new PrimitiveArmor()));
        starting.addAll(CharacterClass.horseOrPony());
        return starting;
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnHideRight(characterAppearance, MyColors.BROWN);
        Looks.putOnHideLeft(characterAppearance, MyColors.BROWN);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x140, MyColors.BROWN, race.getColor(), appearance.getNormalHair(), appearance.getFullBackHair());
    }

    @Override
    public void finalizeLook(CharacterAppearance appearance) {
        Looks.putOnNecklace(appearance);
        if (appearance instanceof AdvancedAppearance) {
            spear.applyYourself((AdvancedAppearance) appearance,
                    appearance.getRace());
        }
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
