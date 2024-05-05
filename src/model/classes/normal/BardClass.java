package model.classes.normal;

import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Looks;
import model.classes.Skill;
import model.items.Item;
import model.items.ObolsDummyItem;
import model.items.accessories.JestersHat;
import model.items.weapons.Club;
import model.items.Equipment;
import model.items.weapons.Scepter;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

import java.util.List;

public class BardClass extends CharacterClass {
    public BardClass() {
        super("Bard", "BRD", 6, 5, false, 18,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Acrobatics, 3),
                        new WeightedSkill(Skill.BluntWeapons, 2),
                        new WeightedSkill(Skill.Entertain, 5),
                        new WeightedSkill(Skill.Leadership, 3),
                        new WeightedSkill(Skill.Logic, 3),
                        new WeightedSkill(Skill.Perception, 4),
                        new WeightedSkill(Skill.Persuade, 6),
                        new WeightedSkill(Skill.Security, 3),
                        new WeightedSkill(Skill.SeekInfo, 4),
                        new WeightedSkill(Skill.Sneak, 4)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnLooseShirt(characterAppearance, MyColors.PURPLE);
        Looks.putOnFancyHat(characterAppearance, MyColors.DARK_PURPLE, MyColors.PURPLE, MyColors.BEIGE);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x60, MyColors.PURPLE, appearance.getBackHairOnly());
    }

    @Override
    public Equipment getStartingEquipment() {
        return new Equipment(new Club());
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }

    @Override
    public void finalizeLook(CharacterAppearance appearance) { }

    @Override
    protected MyColors getIconColor() {
        return MyColors.PURPLE;
    }

    @Override
    protected int getIconNumber() {
        return 0x03;
    }

    @Override
    public String getHowToLearn() {
        return "Sometimes there will be a play in town. I've heard the minstrels sometimes agree to teach about the ways " +
                "of being a bard. If you ever visit a castle, a court jester may do the same.";
    }

    @Override
    public String getDescription() {
        return "Bards are said to have silver tongues, but they're more than just entertainers. " +
                "Bards can handle social situations, spurious combat and the odd bit of skulduggery." +
                "They can also function well as leaders. All things considers, bards are versatile characters. ";
    }

    @Override
    public List<Item> getStartingItems() {
        return List.of(new Scepter(), new JestersHat(), new ObolsDummyItem(150));
    }
}
