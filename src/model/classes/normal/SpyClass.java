package model.classes.normal;

import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Looks;
import model.classes.Skill;
import model.classes.WeightedSkill;
import model.items.Item;
import model.items.accessories.EyeRing;
import model.items.clothing.PlainJerkin;
import model.items.weapons.Dirk;
import model.items.Equipment;
import model.items.weapons.LightCrossbow;
import model.races.Race;
import util.MyPair;
import util.MyTriplet;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.Sprite;
import view.sprites.Sprite8x8;

import java.util.List;

public class SpyClass extends CharacterClass {
    public SpyClass() {
        super("Spy", "SPY", 6, 5, false, 16,
                new WeightedSkill[]{
                        new WeightedSkillMinus(Skill.Academics, 5),
                        new WeightedSkillMinus(Skill.Acrobatics, 2),
                        new WeightedSkill(Skill.Blades, 2),
                        new WeightedSkillMinus(Skill.Entertain, 3),
                        new WeightedSkillPlus(Skill.Logic, 4),
                        new WeightedSkill(Skill.Perception, 6),
                        new WeightedSkillMinus(Skill.Persuade, 4),
                        new WeightedSkillMinus(Skill.Search, 5),
                        new WeightedSkill(Skill.Security, 3),
                        new WeightedSkill(Skill.SeekInfo, 5),
                        new WeightedSkillMinus(Skill.Sneak, 5)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        if (characterAppearance.getGender()) {
            Looks.putOnFancyDress(characterAppearance, MyColors.DARK_BLUE, MyColors.DARK_GRAY);
        } else {
            Looks.putOnTunic(characterAppearance, MyColors.DARK_BLUE);
            putOnFakeMustache(characterAppearance, MyColors.BLACK);
        }
        Looks.putOnBowlersHat(characterAppearance, MyColors.DARK_GRAY);
    }

    public static void putOnFakeMustache(CharacterAppearance characterAppearance, MyColors hairColor) {
        for (int i = 0; i < 3; ++i) {
            Sprite8x8 mush = new Sprite8x8("mush" + i, "face.png", 0xDD+i);
            mush.setColor1(hairColor);
            characterAppearance.addSpriteOnTop(2+i, 4, mush);
        }
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        if (appearance.getGender()) {
            return new AvatarSprite(race, 0x1A0, MyColors.DARK_BLUE, race.getColor(), MyColors.DARK_GRAY,
                    appearance.getBackHairOnly(), appearance.getHalfBackHair(), makeAvatarHat(appearance));
        }
        return new AvatarSprite(race, 0x1C0, MyColors.DARK_BLUE, race.getColor(), MyColors.DARK_GRAY,
                appearance.getBackHairOnly(), appearance.getHalfBackHair(), makeAvatarHat(appearance));
    }

    @Override
    public Equipment getDefaultEquipment() {
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

    @Override
    protected MyColors getIconColor() {
        return MyColors.DARK_BLUE;
    }

    @Override
    protected int getIconNumber() {
        return 0x0C;
    }

    @Override
    public String getHowToLearn() {
        return "Spycraft is not easily learned, but I've heard dark-elves are inclined to become spies. " +
                "Elves can be found woods and in plains.";
    }

    @Override
    public String getDescription() {
        return "Spies are everywhere. Members of this class are often agile, perceptive, clever and " +
                "masters of larceny. While very deft in the skills needed to acquire information, spies " +
                "normally lack much combat ability and general fortitude.";
    }

    @Override
    public List<Item> getStartingItems() {
        return List.of(new LightCrossbow(), new PlainJerkin(), new EyeRing());
    }

    private MyTriplet<Sprite, Sprite, Sprite> makeAvatarHat(CharacterAppearance appearance) {
        return AvatarSprite.makeHat(appearance, "bowlerhat", 0x01,
                MyColors.BLACK, MyColors.DARK_GRAY, MyColors.BEIGE, MyColors.DARK_BLUE);
    }
}
