package model.classes.special;

import model.characters.appearance.CharacterAppearance;
import model.classes.Skill;
import model.classes.WeightedSkill;
import model.items.Equipment;
import model.races.Race;
import model.states.cardgames.sleeves.CardGameSleeve;
import model.states.cardgames.sleeves.NoSleeveAtAll;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.LimitedAvatarSprite;

public class FamiliarClass extends SpecialCharacterClass {
    public FamiliarClass() {
        super("Familiar", "Familiar", 4, 8, false, 0, new WeightedSkill[]{
                new WeightedSkill(Skill.UnarmedCombat, 5)
        });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {

    }

    @Override
    public CardGameSleeve makeCardGameSleeve() {
        return new NoSleeveAtAll();
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new LimitedAvatarSprite(race, 0x360, MyColors.BROWN, MyColors.GOLD,
                MyColors.DARK_BROWN, appearance.getNormalHair());
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
