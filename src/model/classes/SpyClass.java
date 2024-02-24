package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.Item;
import model.items.accessories.EyeRing;
import model.items.clothing.FancyJerkin;
import model.items.clothing.PlainJerkin;
import model.items.weapons.Dirk;
import model.items.Equipment;
import model.items.weapons.LightCrossbow;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.Sprite8x8;

import java.util.List;

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
        Looks.putOnBowlersHat(characterAppearance, MyColors.DARK_GRAY);
        putOnFakeMustache(characterAppearance);
    }

    public static void putOnFakeMustache(CharacterAppearance characterAppearance) {
        for (int i = 0; i < 3; ++i) {
            Sprite8x8 mush = new Sprite8x8("mush" + i, "face.png", 0xDD+i);
            mush.setColor1(MyColors.BLACK);
            characterAppearance.addSpriteOnTop(2+i, 4, mush);
        }
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x57, MyColors.DARK_BLUE, MyColors.DARK_GRAY, appearance.getBackHairOnly());
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
}
