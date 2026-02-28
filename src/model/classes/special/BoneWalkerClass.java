package model.classes.special;

import model.characters.appearance.CharacterAppearance;
import model.classes.Skill;
import model.classes.WeightedSkill;
import model.items.Equipment;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.LimitedAvatarSprite;

public class BoneWalkerClass extends SpecialCharacterClass {
    public BoneWalkerClass() {
        super("BoneWalker", "BoneWalker", 9999, 3, true, 0, new WeightedSkill[]{
                new WeightedSkill(Skill.Blades, 6)
        });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {

    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new LimitedAvatarSprite(race, 0x320, MyColors.PINK, race.getColor(), MyColors.DARK_RED,
                appearance.getNormalHair());
    }

    @Override
    public Equipment getDefaultEquipment() {
        return new Equipment();
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }
}
