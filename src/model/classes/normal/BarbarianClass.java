package model.classes.normal;

import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Looks;
import model.classes.Skill;
import model.classes.WeightedSkill;
import model.items.Equipment;
import model.items.FoodDummyItem;
import model.items.Inventory;
import model.items.Item;
import model.items.clothing.PrimitiveArmor;
import model.items.weapons.GrandMaul;
import model.items.weapons.Hatchet;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

import java.util.List;

public class BarbarianClass extends CharacterClass {
    private static final MyColors CLOTHING_COLOR = MyColors.BROWN;

    public BarbarianClass() {
        super("Barbarian", "BBN", 11, 4, true, 5,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Acrobatics, 1),
                        new WeightedSkill(Skill.Axes, 5),
                        new WeightedSkill(Skill.Blades, 2),
                        new WeightedSkill(Skill.BluntWeapons, 3),
                        new WeightedSkill(Skill.Endurance, 4),
                        new WeightedSkillMinus(Skill.Labor, 4),
                        new WeightedSkillMinus(Skill.Perception, 1),
                        new WeightedSkill(Skill.Survival, 5)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnHideRight(characterAppearance, CLOTHING_COLOR);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x100, CLOTHING_COLOR, race.getColor(), appearance.getNormalHair(), appearance.getFullBackHair());
    }

    @Override
    public Equipment getDefaultEquipment() {
        return new Equipment(new Hatchet());
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }

    @Override
    public void manipulateAvatar(CharacterAppearance appearance, Race race) {
        super.finalizeLook(appearance);
        if (!race.isShort()) {
            appearance.getNormalHair().shiftUpPx(2);
            appearance.getBackHairOnly().shiftUpPx(2);
            appearance.getFullBackHair().shiftUpPx(2);
            appearance.getHalfBackHair().shiftUpPx(2);
        }
    }

    public int getWeaponShift(GameCharacter gameCharacter) {
        if (gameCharacter.getRace().isShort()) {
            return 0;
        }
        return 2;
    }

    @Override
    protected MyColors getIconColor() {
        return MyColors.RED;
    }

    @Override
    protected int getIconNumber() {
        return 0x02;
    }

    @Override
    public String getHowToLearn() {
        return "Barbarians live in hills, mountains and tundra areas. They can sometimes be persuaded to teach you their ways. " +
                "I hear some half-orcs are barbarians. Half-orcs sometimes dwell in hilly areas.";
    }

    @Override
    public String getDescription() {
        return "Barbarian are tribal warriors native to hills and mountainous areas. " +
                "They are heavy fighters who rely on raw power rather than finesse in battle. " +
                "Barbarians rely on their toughness and instincts to survive in the wild and they rarely shy away from " +
                "performing tasks requiring high levels of strength and fortitude.";
    }

    @Override
    public List<Item> getStartingItems() {
        return List.of(new GrandMaul(), new PrimitiveArmor(), new SupplyPackage());
    }

    private static class SupplyPackage extends FoodDummyItem {
        public SupplyPackage() {
            super(50);
        }

        @Override
        public String getName() {
            return "Supplies";
        }

        @Override
        public void addYourself(Inventory inventory) {
            super.addYourself(inventory);
            inventory.addToIngredients(10);
            inventory.addToMaterials(10);
        }

        @Override
        public String getShoppingDetails() {
            return ", A package with 50 rations, 10 ingredients and 10 materials.";
        }
    }
}
