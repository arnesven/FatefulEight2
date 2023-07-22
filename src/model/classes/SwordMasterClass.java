package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.Equipment;
import model.items.weapons.ShortSword;
import model.races.Race;
import view.MyColors;
import view.sprites.*;

public class SwordMasterClass extends SpecialCharacterClass {
    protected SwordMasterClass() {
        super("Sword Master", "SWM", 8, 5, true, 0, new WeightedSkill[]{
                new WeightedSkill(Skill.Acrobatics, 5),
                new WeightedSkill(Skill.Blades, 6),
                new WeightedSkill(Skill.Endurance, 4),
                new WeightedSkill(Skill.Leadership, 4),
                new WeightedSkill(Skill.Perception, 5),
                new WeightedSkill(Skill.Search, 4),
                new WeightedSkill(Skill.SeekInfo, 5),
                new WeightedSkill(Skill.Sneak, 4)
        });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnFancyRobe(characterAppearance, MyColors.DARK_GRAY, MyColors.GRAY);
        addSwordInBack(characterAppearance, MyColors.DARK_GRAY, MyColors.GRAY);
    }

    private void addSwordInBack(CharacterAppearance app, MyColors robeColor, MyColors robeDetailColor) {
        MyColors hiltColor = MyColors.TAN;
        MyColors guardColor = MyColors.GOLD;
        MyColors swordColor = MyColors.LIGHT_GRAY;
        PortraitSprite topLeft = new ClothesSprite(0x89, hiltColor, guardColor);
        app.setSprite(0, 2, topLeft);
        PortraitSprite hilt = new ClothesSprite(0x99, hiltColor, guardColor);
        app.setSprite(0, 3, hilt);
        PortraitSprite guardLeft = new ClothesSprite(0xA9, hiltColor, guardColor);
        guardLeft.setColor4(swordColor);
        app.setSprite(0, 4, guardLeft);
        PortraitSprite guardRight = new ClothesSprite(0xAA, hiltColor, guardColor);
        guardRight.setColor4(swordColor);
        app.setSprite(1, 4, guardRight);
        PortraitSprite shoulderLeft = new ClothesSprite(0xB9, robeColor, guardColor);
        shoulderLeft.setColor4(swordColor);
        app.setSprite(0, 5, shoulderLeft);
        PortraitSprite shoulderRight = new ClothesSprite(0xBA, robeColor, robeDetailColor);
        shoulderRight.setColor4(swordColor);
        app.setSprite(1, 5, shoulderRight);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0xB7, MyColors.DARK_GRAY, MyColors.GRAY, appearance.getNormalHair());
    }

    @Override
    public Equipment getStartingEquipment() {
        return new Equipment(new ShortSword());
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }
}
