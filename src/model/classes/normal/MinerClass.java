package model.classes.normal;

import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Looks;
import model.classes.Skill;
import model.horses.HorseItemAdapter;
import model.horses.Pony;
import model.horses.Prancer;
import model.items.Equipment;
import model.items.Item;
import model.items.clothing.RustyRingMail;
import model.items.weapons.Hatchet;
import model.items.weapons.Pickaxe;
import model.items.weapons.RustyPickaxe;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.ClothesSpriteWithBack;

import java.util.ArrayList;
import java.util.List;

public class MinerClass extends CharacterClass {
    public MinerClass() {
        super("Miner", "MIN", 12, 3, true, 10,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Axes, 5),
                        new WeightedSkill(Skill.BluntWeapons, 3),
                        new WeightedSkill(Skill.Endurance, 5),
                        new WeightedSkill(Skill.Entertain, 1),
                        new WeightedSkill(Skill.Labor, 5),
                        new WeightedSkill(Skill.Perception, 2),
                        new WeightedSkill(Skill.Search, 4),
                        new WeightedSkill(Skill.Survival, 2)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnTunic(characterAppearance, MyColors.DARK_GRAY);
        putOnLampHelmet(characterAppearance);
    }

    public static void putOnLampHelmet(CharacterAppearance characterAppearance) {
        characterAppearance.removeOuterHair();
        for (int y = 0; y <= 2; ++y) {
            for (int x = 2; x <= 4; ++x) {
                characterAppearance.setSprite(x, y, new ClothesSpriteWithBack(0x30 + 0x10 * y + x + 5, MyColors.GRAY, MyColors.DARK_GRAY));
            }
        }
        characterAppearance.getSprite(3, 1).setColor4(MyColors.LIGHT_YELLOW);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0xE8, MyColors.DARK_GRAY, MyColors.LIGHT_YELLOW,
                appearance.getBackHairOnly(), appearance.getHalfBackHair());
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
    protected MyColors getIconColor() {
        return MyColors.YELLOW;
    }

    @Override
    protected int getIconNumber() {
        return 0x0E;
    }

    @Override
    public String getHowToLearn() {
        return "Miners can be found in mines in the mountains. Dwarves are often miners. " +
                "Dwarves can be found in hills and mountains.";
    }

    @Override
    public String getDescription() {
        return "Miners are laborers who dig and hack in the earth after precious crystals and ore. " +
                "They are proficient with heavy weapons like axes and hammers and have great fortitude.";
    }

    @Override
    public List<Item> getStartingItems() {
        List<Item> list = new ArrayList<Item>(List.of(new Pickaxe(), new RustyRingMail()));
        list.addAll(CharacterClass.horseOrPony());
        return list;
    }
}
