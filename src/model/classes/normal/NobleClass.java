package model.classes.normal;

import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Looks;
import model.classes.Skill;
import model.classes.WeightedSkill;
import model.items.Equipment;
import model.items.GoldDummyItem;
import model.items.Item;
import model.items.accessories.Crown;
import model.items.weapons.Rapier;
import model.items.weapons.TrainingBow;
import model.races.Race;
import util.MyPair;
import util.MyTriplet;
import view.MyColors;
import view.sprites.*;

import java.util.List;

public class NobleClass extends CharacterClass {
    private static final MyColors CLOTHES_COLOR = MyColors.BLUE;
    private static final MyColors DETAIL_COLOR = MyColors.CYAN;

    public NobleClass() {
        super("Noble", "N", 7, 4, false, 30,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Academics, 4),
                        new WeightedSkill(Skill.Acrobatics, 1),
                        new WeightedSkillMinus(Skill.Blades, 3),
                        new WeightedSkill(Skill.Bows, 3),
                        new WeightedSkill(Skill.Entertain, 4),
                        new WeightedSkill(Skill.Leadership, 5),
                        new WeightedSkill(Skill.Logic, 2),
                        new WeightedSkill(Skill.Mercantile, 4),
                        new WeightedSkillMinus(Skill.Persuade, 5),
                        new WeightedSkill(Skill.SeekInfo, 4),
                        new WeightedSkill(Skill.Sneak, 1)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        if (characterAppearance.getGender()) {
            Looks.putOnFancyDress(characterAppearance, CLOTHES_COLOR, DETAIL_COLOR);
        } else {
            Looks.putOnFancyRobe(characterAppearance, CLOTHES_COLOR, DETAIL_COLOR);
        }
        putOnCrown(characterAppearance, MyColors.LIGHT_GRAY, MyColors.LIGHT_GRAY);
    }

    public static void putOnCrown(CharacterAppearance characterAppearance, MyColors crownColor, MyColors gemstoneColor) {
        characterAppearance.removeOuterHair();
        int spriteOffset =  0xC8;
        if (characterAppearance.hairOnTop()) {
            spriteOffset = 0xA8;
        }
        for (int y = 1; y <= 2; ++y) {
            for (int x = 2; x <= 4; ++x) {
                PortraitSprite spr = new FaceAndClothesSpriteWithBack(spriteOffset + 0x10 * y + x, characterAppearance.getHairColor(), crownColor);
                spr.setColor4(gemstoneColor);
                characterAppearance.setSprite(x, y, spr);
            }
        }
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        if (appearance.getGender()) {
            return new AvatarSprite(race, 0x440, CLOTHES_COLOR, race.getColor(), DETAIL_COLOR,
                    appearance.getBackHairOnly(), appearance.getHalfBackHair(), makeAvatarCrown(appearance));
        }
        return new AvatarSprite(race, AvatarSprite.FANCY_MALE_BASE, CLOTHES_COLOR, race.getColor(), DETAIL_COLOR,
                appearance.getBackHairOnly(), appearance.getHalfBackHair(), makeAvatarCrown(appearance));
    }

    @Override
    public Equipment getDefaultEquipment() {
        return new Equipment(new TrainingBow());
    }

    @Override
    public boolean isBackRowCombatant() {
        return true;
    }

    @Override
    protected MyColors getIconColor() {
        return MyColors.CYAN;
    }

    @Override
    protected int getIconNumber() {
        return 0x10;
    }

    @Override
    public String getHowToLearn() {
        return "Noblemen can usually be found in castles, but sometimes they travel with their entourage on the road. " +
                "Halflings are sometimes noblemen. Halflings can be found in fields and in the woods.";
    }

    @Override
    public String getDescription() {
        return "Nobles are the leaders and rulers of society. Nobles have longer educations than most and are skilled " +
                "broadly in both martial skills and academics. From an early age they are instructed how to act during " +
                "social gatherings and adult nobles are often able to mix negotiation, persuasion and intimidation " +
                "to get what they want.";
    }

    @Override
    public List<Item> getStartingItems() {
        return List.of(new Rapier(), new Crown(), new GoldDummyItem(20));
    }

    private MyTriplet<Sprite, Sprite, Sprite> makeAvatarCrown(CharacterAppearance appearance) {
        return AvatarSprite.makeHat(appearance, "crown", 0x00,
                MyColors.BLACK, MyColors.BROWN, MyColors.RED, MyColors.PINK);
    }
}
