package model.classes.normal;

import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Looks;
import model.classes.Skill;
import model.classes.WeightedSkill;
import model.items.Inventory;
import model.items.InventoryDummyItem;
import model.items.Item;
import model.items.accessories.DeftGloves;
import model.items.clothing.PlainJerkin;
import model.items.weapons.Dirk;
import model.items.Equipment;
import model.items.weapons.TwinDaggers;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;
import view.sprites.Sprite8x8;

import java.util.List;

public class ThiefClass extends CharacterClass {
    public ThiefClass() {
        super("Thief", "T", 6, 6, false, 12,
                new WeightedSkill[]{
                        new WeightedSkillMinus(Skill.Acrobatics, 4),
                        new WeightedSkill(Skill.Blades, 3),
                        new WeightedSkill(Skill.BluntWeapons, 1),
                        new WeightedSkill(Skill.Labor, 2),
                        new WeightedSkill(Skill.Logic, 3),
                        new WeightedSkillPlus(Skill.Perception, 4),
                        new WeightedSkillMinus(Skill.Persuade, 3),
                        new WeightedSkill(Skill.Mercantile, 3),
                        new WeightedSkill(Skill.Search, 3),
                        new WeightedSkill(Skill.Security, 5),
                        new WeightedSkillMinus(Skill.SeekInfo, 3),
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
        return new AvatarSprite(race, 0x00, MyColors.BROWN, race.getColor(), appearance.getFacialOnly(), CharacterAppearance.noHair());
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

    @Override
    public List<Item> getStartingItems() {
        return List.of(new TwinDaggers(), new PlainJerkin(), new LarcenyKit());
    }

    private static class LarcenyKit extends InventoryDummyItem {
        private final Sprite SPRITE = new ItemSprite(1, 0xA, MyColors.GRAY, MyColors.DARK_GRAY);

        public LarcenyKit() {
            super("Larceny Kit", 17);
        }

        @Override
        protected Sprite getSprite() {
            return SPRITE;
        }

        @Override
        public int getWeight() {
            return 0;
        }

        @Override
        public void addYourself(Inventory inventory) {
            inventory.add(new DeftGloves());
            inventory.addToLockpicks(2);
        }

        @Override
        public String getShoppingDetails() {
            return ", Deft Gloves and 2 Lockpicks.";
        }

        @Override
        public String getDescription() {
            return "Deft Gloves and 2 lockpicks.";
        }

        @Override
        public Item copy() {
            return new LarcenyKit();
        }
    }
}
