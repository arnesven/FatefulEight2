package model.classes;

import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.items.Equipment;
import model.items.FoodDummyItem;
import model.items.Item;
import model.items.clothing.FurArmor;
import model.items.weapons.GrandMaul;
import model.items.weapons.Hatchet;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

import java.util.List;

public class BarbarianClass extends CharacterClass {
    private static final MyColors CLOTHING_COLOR = MyColors.BROWN;

    protected BarbarianClass() {
        super("Barbarian", "BBN", 11, 4, true, 5,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Acrobatics, 1),
                        new WeightedSkill(Skill.Axes, 5),
                        new WeightedSkill(Skill.Blades, 2),
                        new WeightedSkill(Skill.BluntWeapons, 3),
                        new WeightedSkill(Skill.Endurance, 5),
                        new WeightedSkill(Skill.Labor, 4),
                        new WeightedSkill(Skill.Perception, 1),
                        new WeightedSkill(Skill.Survival, 5)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnHideRight(characterAppearance, CLOTHING_COLOR);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x80, CLOTHING_COLOR, appearance.getNormalHair());
    }

    @Override
    public Equipment getStartingEquipment() {
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
            appearance.getNormalHair().shiftUpPx(appearance.getNormalHair().getUpShift() + 1);
            appearance.getBackHairOnly().shiftUpPx(appearance.getBackHairOnly().getUpShift() + 1);
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
        return List.of(new GrandMaul(), new FurArmor(), new FoodDummyItem(50));
    }
}
