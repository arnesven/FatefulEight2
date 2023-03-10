package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.weapons.Dirk;
import model.items.Equipment;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.Sprite8x8;

public class SpyClass extends CharacterClass {
    protected SpyClass() {
        super("Spy", "SPY", 6, 5, false, 0,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Acrobatics, 2),
                        new WeightedSkill(Skill.Blades, 2),
                        new WeightedSkill(Skill.Logic, 5),
                        new WeightedSkill(Skill.Perception, 6),
                        new WeightedSkill(Skill.Persuade, 4),
                        new WeightedSkill(Skill.Search, 5),
                        new WeightedSkill(Skill.Security, 5),
                        new WeightedSkill(Skill.SeekInfo, 5),
                        new WeightedSkill(Skill.Sneak, 5)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnTunic(characterAppearance, MyColors.DARK_BLUE);
        Looks.putOnCap(characterAppearance, MyColors.DARK_BLUE);
        putOnFakeMustache(characterAppearance);
    }

    private void putOnFakeMustache(CharacterAppearance characterAppearance) {
        for (int i = 0; i < 3; ++i) {
            Sprite8x8 mush = new Sprite8x8("mush" + i, "face.png", 0xDD+i);
            mush.setColor1(MyColors.BLACK);
            characterAppearance.addSpriteOnTop(2+i, 4, mush);
        }
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x57, MyColors.DARK_BLUE, appearance.getBackHairOnly());
    }

    @Override
    public Equipment getStartingEquipment() {
        return new Equipment(new Dirk());
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }

    @Override
    public void finalizeLook(CharacterAppearance appearance) {
        Looks.finalizeCap(appearance);
    }
}
