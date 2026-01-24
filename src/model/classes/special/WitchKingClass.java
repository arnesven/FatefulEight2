package model.classes.special;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.classes.WeightedSkill;
import model.classes.normal.NobleClass;
import model.classes.Skill;
import model.items.Equipment;
import model.items.accessories.Crown;
import model.items.clothing.MesmersRobes;
import model.items.weapons.OldStaff;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class WitchKingClass extends SpecialCharacterClass {
    public WitchKingClass() {
        super("Witch King", "WKN", 8, 3, false, 0, new WeightedSkill[] {
                new WeightedSkill(Skill.Blades, 4),
                new WeightedSkill(Skill.Leadership, 3),
                new WeightedSkill(Skill.Logic, 3),
                new WeightedSkill(Skill.MagicGreen, 4),
                new WeightedSkill(Skill.MagicWhite, 4),
                new WeightedSkill(Skill.MagicRed, 5),
                new WeightedSkill(Skill.MagicBlue, 5),
                new WeightedSkill(Skill.MagicBlack, 5),
                new WeightedSkill(Skill.Perception, 2),
                new WeightedSkill(Skill.SpellCasting, 4),
        });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnTunic(characterAppearance, MyColors.DARK_RED);
        NobleClass.putOnCrown(characterAppearance, MyColors.GOLD, MyColors.GOLD);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x340, MyColors.DARK_RED, race.getColor(),
                MyColors.YELLOW, appearance.getBackHairOnly(), appearance.getHalfBackHair());
    }

    @Override
    public Equipment getDefaultEquipment() {
        return new Equipment(new OldStaff(), new MesmersRobes(), new Crown());
    }

    @Override
    public boolean isBackRowCombatant() {
        return true;
    }
}
