package model.classes.normal;

import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.characters.appearance.GnarledStaffDetail;
import model.classes.CharacterClass;
import model.classes.Looks;
import model.classes.Skill;
import model.classes.WeightedSkill;
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
    private final GnarledStaffDetail staff;

    public DruidClass() {
        super("Druid", "D", 6, 4, false, 12,
                new WeightedSkill[]{
                        new WeightedSkillPlus(Skill.Blades, 2),
                        new WeightedSkill(Skill.BluntWeapons, 3),
                        new WeightedSkill(Skill.Endurance, 4),
                        new WeightedSkill(Skill.Labor, 3),
                        new WeightedSkillPlus(Skill.Leadership, 3),
                        new WeightedSkill(Skill.Logic, 3),
                        new WeightedSkill(Skill.MagicGreen, 5),
                        new WeightedSkill(Skill.Sneak, 2),
                        new WeightedSkill(Skill.SpellCasting, 2),
                        new WeightedSkillPlus(Skill.Survival, 5)
                });
        this.staff = new GnarledStaffDetail();
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnRobe(characterAppearance, MyColors.DARK_GREEN, MyColors.GREEN);
        Looks.putOnHood(characterAppearance, MyColors.DARK_GREEN);
        if (characterAppearance instanceof AdvancedAppearance) {
            staff.applyYourself((AdvancedAppearance) characterAppearance,
                    characterAppearance.getRace());
        }
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0xE0, MyColors.DARK_GREEN, race.getColor(),
                appearance.getFacialOnly(), CharacterAppearance.noHair());
    }

    @Override
    public Equipment getDefaultEquipment() {
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
        return List.of(new SummonFamiliarSpell(), new GiantGrowthSpell(), new AlchemyPackage());
    }

    private static class AlchemyPackage extends InventoryDummyItem {
        private final Sprite SPRITE = new ItemSprite(14, 7, MyColors.WHITE,
                MyColors.LIGHT_GREEN, MyColors.CYAN);

        public AlchemyPackage() {
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
        public String getDescription() {
            return "20 ingredients and the Alchemy spell";
        }

        @Override
        public Item copy() {
            return new AlchemyPackage();
        }
    }
}
