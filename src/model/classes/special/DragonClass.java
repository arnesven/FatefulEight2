package model.classes.special;

import model.characters.appearance.CharacterAppearance;
import model.classes.Skill;
import model.classes.WeightedSkill;
import model.enemies.DragonEnemy;
import model.items.Equipment;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class DragonClass extends SpecialCharacterClass {
    private final DragonEnemy dragon;

    public DragonClass(DragonEnemy dragon) {
        super("Dragon", "DGN", dragon.getMaxHP() - 1,
                dragon.getSpeed(), false, 0,
                new WeightedSkill[]{new WeightedSkill(Skill.UnarmedCombat, 5)});
        this.dragon = dragon;
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) { }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        AvatarSprite spr = new AvatarSprite(race, 0x370, MyColors.BEIGE, dragon.getColorSet()[1], MyColors.GRAY,
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
