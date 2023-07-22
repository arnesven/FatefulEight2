package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.Equipment;
import model.items.accessories.Crown;
import model.items.clothing.MesmersRobes;
import model.items.weapons.OldStaff;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class WitchKingClass extends SpecialCharacterClass {
    protected WitchKingClass() {
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
        NobleClass.putOnCrown(characterAppearance);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0xA0, MyColors.DARK_RED, MyColors.YELLOW, appearance.getBackHairOnly());
    }

    @Override
    public Equipment getStartingEquipment() {
        return new Equipment(new OldStaff(), new MesmersRobes(), new Crown());
    }

    @Override
    public boolean isBackRowCombatant() {
        return true;
    }
}
