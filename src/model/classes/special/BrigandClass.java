package model.classes.special;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.classes.Skill;
import model.classes.WeightedSkill;
import model.classes.normal.WeightedSkillMinus;
import model.classes.normal.WeightedSkillPlus;
import model.items.Equipment;
import model.items.weapons.Dirk;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.FaceAndClothesSprite;
import view.sprites.PortraitSprite;
import view.sprites.Sprite8x8;

public class BrigandClass extends SpecialCharacterClass {
    public BrigandClass() {
        super("Brigand", "BRI", 10, 6, false, 28,
                new WeightedSkill[]{
                new WeightedSkillMinus(Skill.Acrobatics, 5),
                new WeightedSkill(Skill.Blades, 5),
                new WeightedSkillPlus(Skill.Entertain, 3),
                new WeightedSkillPlus(Skill.Perception, 4),
                new WeightedSkillMinus(Skill.Persuade, 3),
                new WeightedSkill(Skill.Mercantile, 3),
                new WeightedSkill(Skill.Search, 5),
                new WeightedSkill(Skill.Security, 5),
                new WeightedSkillMinus(Skill.SeekInfo, 3),
                new WeightedSkill(Skill.Sneak, 5)}
    );
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnFancyRobe(characterAppearance, MyColors.DARK_RED, MyColors.DARK_RED);
        putOnLargeHat(characterAppearance, MyColors.DARK_RED);
        putOnMask(characterAppearance, MyColors.DARK_RED);
    }

    private void putOnMask(CharacterAppearance characterAppearance, MyColors fill) {
        for (int i = 0; i < 4; ++i) {
            Sprite8x8 mask = new Sprite8x8("brigandmask" + i, "face.png", 0x22C+i);
            mask.setColor1(fill);
            characterAppearance.addSpriteOnTop(2+i, 3, mask);
        }
    }

    private void putOnLargeHat(CharacterAppearance appearance, MyColors fillColor) {
        appearance.removeOuterHair();
        for (int y = 1; y < 3; ++y) {
            for (int x = 1; x < 6; ++x) {
                MyColors color2 = y == 2 ? appearance.getHairColor() : MyColors.BLACK;
                PortraitSprite spr =  new FaceAndClothesSprite(0x1E4 + 0x10 * y + x, color2, fillColor);
                appearance.setSprite(x, y, spr);
            }
        }
        appearance.setSprite(0,2, new FaceAndClothesSprite(0x23D, appearance.getHairColor(), fillColor));
        appearance.setSprite(1,2, new FaceAndClothesSprite(0x23E, appearance.getHairColor(), fillColor));
        appearance.setSprite(5,2, new FaceAndClothesSprite(0x24D, appearance.getHairColor(), fillColor));
        appearance.setSprite(6,2, new FaceAndClothesSprite(0x24E, appearance.getHairColor(), fillColor));
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x400, MyColors.DARK_RED, race.getColor(), MyColors.DARK_RED,
                appearance.getBackHairOnly(), appearance.getHalfBackHair());
    }

    @Override
    public Equipment getDefaultEquipment() {
        return  new Equipment(new Dirk());
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }
}
