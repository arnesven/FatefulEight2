package model.classes.normal;

import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Looks;
import model.classes.Skill;
import model.items.Item;
import model.items.spells.BurningWeaponSpell;
import model.items.spells.EntropicBoltSpell;
import model.items.spells.FireWallSpell;
import model.items.spells.WeakenSpell;
import model.items.weapons.Club;
import model.items.Equipment;
import model.items.weapons.MagesStaff;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.ClothesSpriteWithBack;

import java.util.List;

public class SorcererClass extends CharacterClass {
    public SorcererClass() {
        super("Sorcerer", "SOR", 8, 4, false, 28,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Blades, 2),
                        new WeightedSkill(Skill.BluntWeapons, 4),
                        new WeightedSkill(Skill.Endurance, 2),
                        new WeightedSkill(Skill.Logic, 4),
                        new WeightedSkill(Skill.MagicBlack, 2),
                        new WeightedSkill(Skill.MagicRed, 3),
                        new WeightedSkill(Skill.Persuade, 2),
                        new WeightedSkill(Skill.Sneak, 3),
                        new WeightedSkill(Skill.SpellCasting, 5)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnRobe(characterAppearance, MyColors.DARK_GRAY, MyColors.RED);
        putOnHelm(characterAppearance);
    }

    private static void putOnHelm(CharacterAppearance characterAppearance) {
        characterAppearance.removeOuterHair();
        for (int y = 0; y <= 2; ++y) {
            for (int x = 2; x <= 4; ++x) {
                characterAppearance.setSprite(x, y, new ClothesSpriteWithBack(0x10 * y + x + 8, MyColors.DARK_GRAY, MyColors.BLACK));
            }
        }
        characterAppearance.getSprite(3, 2).setColor4(MyColors.RED);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x180, MyColors.DARK_GRAY, race.getColor(), MyColors.RED,
                appearance.getBackHairOnly(), appearance.getHalfBackHair());
    }

    @Override
    public Equipment getDefaultEquipment() {
        return new Equipment(new Club());
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }

    @Override
    protected MyColors getIconColor() {
        return MyColors.RED;
    }

    @Override
    protected int getIconNumber() {
        return 0x0A;
    }

    @Override
    public String getHowToLearn() {
        return "I've heard sorcerers often dwell in towers in the woods. Perhaps if you could gain entry, " +
                "the sorcerer would divulge his secrets to you?";
    }

    @Override
    public String getDescription() {
        return "Sorcerers are mages who primarily delve into black and red magic. They tend to be more reckless " +
                "than Wizards, and more ambitious in their designs than witches and magicians. Sorcerer's do not rule " +
                "out violence as a solutions when the situation requires it.";
    }

    @Override
    public List<Item> getStartingItems() {
        return List.of(new MagesStaff(), new FireWallSpell(), new WeakenSpell());
    }
}
