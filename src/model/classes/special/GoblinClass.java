package model.classes.special;

import model.characters.appearance.CharacterAppearance;
import model.classes.Skill;
import model.classes.WeightedSkill;
import model.items.Equipment;
import model.items.accessories.SkullCap;
import model.items.clothing.LeatherArmor;
import model.items.weapons.Glaive;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class GoblinClass extends SpecialCharacterClass {
    public GoblinClass() {
        super("Goblin", "GBN", 5, 5, true, 0,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Axes, 4),
                        new WeightedSkill(Skill.Bows, 4),
                        new WeightedSkill(Skill.BluntWeapons, 4),
                        new WeightedSkill(Skill.MagicBlack, 5),
                        new WeightedSkill(Skill.MagicRed, 5),
                        new WeightedSkill(Skill.MagicBlue, 5),
                        new WeightedSkill(Skill.Polearms, 5),
                        new WeightedSkill(Skill.Search, 3),
                        new WeightedSkill(Skill.Security, 4),
                        new WeightedSkill(Skill.Sneak, 5)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) { }

    @Override
    public boolean showFacialHair() {
        return false;
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x300, MyColors.BEIGE, race.getColor(), appearance.getNormalHair(), CharacterAppearance.noHair());
    }

    @Override
    public Equipment getDefaultEquipment() {
        return new Equipment(new Glaive(), new LeatherArmor(), new SkullCap());
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }
}
