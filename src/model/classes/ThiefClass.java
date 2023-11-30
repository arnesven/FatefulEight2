package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.weapons.Dirk;
import model.items.Equipment;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.Sprite8x8;

public class ThiefClass extends CharacterClass {
    protected ThiefClass() {
        super("Thief", "T", 6, 6, false, 0,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Acrobatics, 4),
                        new WeightedSkill(Skill.Blades, 3),
                        new WeightedSkill(Skill.BluntWeapons, 1),
                        new WeightedSkill(Skill.Entertain, 2),
                        new WeightedSkill(Skill.Logic, 3),
                        new WeightedSkill(Skill.Perception, 5),
                        new WeightedSkill(Skill.Persuade, 4),
                        new WeightedSkill(Skill.Search, 5),
                        new WeightedSkill(Skill.Security, 5),
                        new WeightedSkill(Skill.SeekInfo, 4),
                        new WeightedSkill(Skill.Sneak, 5)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnTunic(characterAppearance, MyColors.BROWN);
        Looks.putOnHood(characterAppearance, MyColors.BROWN);
        putOnThiefsMask(characterAppearance);
    }

    public static void putOnThiefsMask(CharacterAppearance characterAppearance) {
        for (int i = 0; i < 3; ++i) {
            Sprite8x8 mask = new Sprite8x8("thiefmask" + i, "face.png", 0x1EA+i);
            mask.setColor1(MyColors.BLACK);
            characterAppearance.addSpriteOnTop(2+i, 3, mask);
        }
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x00, MyColors.BROWN, appearance.getFacialOnly());
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
    public void manipulateAvatar(CharacterAppearance appearance, Race race) {
        super.finalizeLook(appearance);
        if (race.isShort()) {
            appearance.getFacialOnly().shiftUpPx(-2);
        }
    }

    @Override
    public boolean coversEars() {
        return true;
    }

    @Override
    public boolean showHairInBack() {
        return false;
    }

    @Override
    protected MyColors getIconColor() {
        return MyColors.BROWN;
    }

    @Override
    protected int getIconNumber() {
        return 0x08;
    }

    @Override
    public String getHowToLearn() {
        return "Thieves are common in towns. They are however, more likely to cut your purse, than to guide you in " +
                "the ways of skullduggery.";
    }

    @Override
    public String getDescription() {
        return "Anywhere there are people living in urban dwellings, there are thieves. " +
                "Thieves rely heavily on their senses and their dexterity to. Be it lifting purses, " +
                "prying a safe open, or obtaining that rare prize, the thief uses all skills at her disposal " +
                "to achieve success.";
    }
}
