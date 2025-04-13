package model.classes.special;

import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Skill;
import model.items.Equipment;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

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
        AvatarSprite spr = new AvatarSprite(race, 0x364, MyColors.GRAY_RED, MyColors.BROWN, MyColors.DARK_GREEN,
                appearance.getNormalHair(), CharacterAppearance.noHair());
        return spr;
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
