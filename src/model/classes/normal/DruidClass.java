package model.classes.normal;

import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Looks;
import model.classes.Skill;
import model.items.*;
import model.items.spells.AlchemySpell;
import model.items.spells.GiantGrowthSpell;
import model.items.spells.SummonFamiliarSpell;
import model.items.weapons.Club;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class DruidClass extends CharacterClass {
    public DruidClass() {
        super("Druid", "D", 6, 4, false, 12,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Blades, 2),
                        new WeightedSkill(Skill.BluntWeapons, 4),
                        new WeightedSkill(Skill.Endurance, 4),
                        new WeightedSkill(Skill.Labor, 3),
                        new WeightedSkill(Skill.Leadership, 3),
                        new WeightedSkill(Skill.Logic, 3),
                        new WeightedSkill(Skill.MagicGreen, 5),
                        new WeightedSkill(Skill.Sneak, 2),
                        new WeightedSkill(Skill.SpellCasting, 2),
                        new WeightedSkill(Skill.Survival, 5)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnRobe(characterAppearance, MyColors.DARK_GREEN, MyColors.GREEN);
        Looks.putOnHood(characterAppearance, MyColors.DARK_GREEN);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x70, MyColors.DARK_GREEN, appearance.getFacialOnly());
    }

    @Override
    public Equipment getStartingEquipment() {
        return new Equipment(new Club());
    }

    @Override
    public boolean isBackRowCombatant() {
        return true;
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
        return MyColors.DARK_GREEN;
    }

    @Override
    protected int getIconNumber() {
        return 0x06;
    }

    @Override
    public String getHowToLearn() {
        return "Look for stone circles out on the plains. Druids sometimes use those for worship and rituals. They may " +
                "agree to teach you their ways. On the plains you may also find nomads. Nomads sometimes practice druidism.";
    }

    @Override
    public String getDescription() {
        return "Druids are nature mages who are highly attuned with Green Magic. They often live " +
                "as hermits which require them to have good survival skills and fair combat abilities. " +
                "Druids are often highly intelligent and can make good decisions as leaders.";
    }

    @Override
    public List<Item> getStartingItems() {
        return List.of(new SummonFamiliarSpell(), new GiantGrowthSpell(), new AlchmyPackage());
    }

    private class AlchmyPackage extends InventoryDummyItem {
        private final Sprite SPRITE = new ItemSprite(14, 7, MyColors.WHITE,
                MyColors.LIGHT_GREEN, MyColors.CYAN);

        public AlchmyPackage() {
            super("Alchemy Package", 10);
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
            inventory.add(new AlchemySpell());
            inventory.addToIngredients(20);
        }

        @Override
        public String getShoppingDetails() {
            return ", Alchemy Spell and 20 Ingredients.";
        }

        @Override
        public Item copy() {
            return new AlchmyPackage();
        }
    }
}
