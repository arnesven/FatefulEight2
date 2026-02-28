package model.classes.special;

import model.characters.appearance.CharacterAppearance;
import model.classes.Skill;
import model.classes.WeightedSkill;
import model.items.Equipment;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.LimitedAvatarSprite;

public class TreeHerderClass extends SpecialCharacterClass {
    public TreeHerderClass() {
        super("TreeHerder", "TreeHerder", 18, 4, true, 0,
                new WeightedSkill[]{new WeightedSkill(Skill.BluntWeapons, 6)});
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {

    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new LimitedAvatarSprite(race, 0x364, MyColors.GRAY_RED, MyColors.BROWN, MyColors.DARK_GREEN,
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
