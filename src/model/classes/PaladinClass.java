package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.Item;
import model.items.clothing.RustyChestPlate;
import model.items.spells.TurnUndeadSpell;
import model.items.weapons.Club;
import model.items.Equipment;
import model.items.weapons.Warhammer;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

import java.util.List;

public class PaladinClass extends CharacterClass {

    private static final MyColors UNDERSHIRT_COLOR = MyColors.GRAY;
    private static final MyColors ARMOR_COLOR = MyColors.WHITE;

    protected PaladinClass() {
        super("Paladin", "PAL", 10, 4, true, 22,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Blades, 4),
                        new WeightedSkill(Skill.BluntWeapons, 4),
                        new WeightedSkill(Skill.Endurance, 3),
                        new WeightedSkill(Skill.Labor, 1),
                        new WeightedSkill(Skill.Leadership, 4),
                        new WeightedSkill(Skill.MagicWhite, 3),
                        new WeightedSkill(Skill.Perception, 2),
                        new WeightedSkill(Skill.Polearms, 4),
                        new WeightedSkill(Skill.Survival, 2)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnArmor(characterAppearance, ARMOR_COLOR, UNDERSHIRT_COLOR);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x10, MyColors.WHITE, appearance.getNormalHair());
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
    protected MyColors getIconColor() {
        return MyColors.WHITE;
    }

    @Override
    protected int getIconNumber() {
        return 0x0F;
    }

    @Override
    public String getHowToLearn() {
        return "Paladins can sometimes be found at temples. Some High-Elves are paladins. " +
                "Elves can be found in woods and in plains.";
    }

    @Override
    public String getDescription() {
        return "Paladins are noble knights and grand protectors of the Order of Light. They possess powerful " +
                "combat skills, and are proficient with blades, blunt weapons and polearms. They are natural leaders " +
                "and have a little affinity for white magic";
    }

    @Override
    public List<Item> getStartingItems() {
        return List.of(new Warhammer(), new RustyChestPlate(), new TurnUndeadSpell());
    }
}
