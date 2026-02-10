package model.classes.normal;

import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Looks;
import model.classes.Skill;
import model.classes.WeightedSkill;
import model.items.Equipment;
import model.items.Item;
import model.items.MaterialsDummyItem;
import model.items.clothing.StuddedTunic;
import model.items.weapons.Hatchet;
import model.items.weapons.Longsword;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

import java.util.List;

public class ArtisanClass extends CharacterClass {
    private static final MyColors APRON_COLOR = MyColors.BROWN;
    private static final MyColors SHIRT_COLOR = MyColors.LIGHT_BLUE;

    public ArtisanClass() {
        super("Artisan", "ART", 7, 4, false, 25,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Academics, 2),
                        new WeightedSkill(Skill.Axes, 3),
                        new WeightedSkillMinus(Skill.Blades, 3),
                        new WeightedSkill(Skill.Entertain, 2),
                        new WeightedSkill(Skill.Labor, 5),
                        new WeightedSkillPlus(Skill.Logic, 4),
                        new WeightedSkill(Skill.Mercantile, 5),
                        new WeightedSkill(Skill.Perception, 4),
                        new WeightedSkill(Skill.Persuade, 2),
                        new WeightedSkill(Skill.Security, 4),
                        new WeightedSkill(Skill.SeekInfo, 2)
                });
    }

    @Override
    public String getDescription() {
        return "Artisans are the crafters, tailors, cobblers, smiths and woodworkers of the world. " +
                "Handy with tools, they have a keen eye for what will work, what's stylish and proper. " +
                "Artisans are often well connected and are comfortable with social interaction.";
    }

    @Override
    public List<Item> getStartingItems() {
        return List.of(new Longsword(), new StuddedTunic(), new MaterialsDummyItem(15));
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnTunic(characterAppearance, SHIRT_COLOR);
        Looks.putOnApron(characterAppearance, APRON_COLOR, SHIRT_COLOR);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x1E0, MyColors.LIGHT_BLUE, race.getColor(), MyColors.BROWN, appearance.getNormalHair(), appearance.getFullBackHair());
    }

    @Override
    public Equipment getDefaultEquipment() {
        return new Equipment(new Hatchet());
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }

    @Override
    protected MyColors getIconColor() {
        return MyColors.PEACH;
    }

    @Override
    protected int getIconNumber() {
        return 0x01;
    }

    @Override
    public String getHowToLearn() {
        return "It is not uncommon to find artisans travelling on roads. They will often agree to teach you their trade. " +
                "I've heard that many dwarves are artisans. Dwarves can be found in hills and mountains.";
    }
}
