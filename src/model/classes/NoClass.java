package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.Equipment;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class NoClass extends CharacterClass {
    protected NoClass() {
        super("", "NONE", 3, 3, false, 0,
                new WeightedSkill[]{});
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnLooseShirt(characterAppearance, MyColors.BEIGE);
    }

    @Override
    public AvatarSprite getAvatar(Race race) {
        return new AvatarSprite(race, 0x87, MyColors.BEIGE);
    }

    @Override
    public Equipment getStartingEquipment() {
        return new Equipment();
    }

    @Override
    public boolean isBackRowCombatant() {
        return true;
    }
}
