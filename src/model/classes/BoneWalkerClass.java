package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.Equipment;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class BoneWalkerClass extends SpecialCharacterClass {
    protected BoneWalkerClass() {
        super("BoneWalker", "BoneWalker", 9999, 3, true, 0, new WeightedSkill[]{
                new WeightedSkill(Skill.Blades, 6)
        });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {

    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x90, MyColors.PINK, appearance.getNormalHair());
    }

    @Override
    public Equipment getStartingEquipment() {
        return new Equipment();
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }
}
