package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.Equipment;
import model.items.GoldDummyItem;
import model.items.Item;
import model.items.accessories.Crown;
import model.items.weapons.Longsword;
import model.items.weapons.Rapier;
import model.items.weapons.TrainingBow;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.FaceAndClothesSpriteWithBack;

import java.util.List;

public class NobleClass extends CharacterClass {
    private static final MyColors CLOTHES_COLOR = MyColors.BLUE;
    private static final MyColors DETAIL_COLOR = MyColors.CYAN;

    protected NobleClass() {
        super("Noble", "N", 7, 5, false, 35,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Acrobatics, 1),
                        new WeightedSkill(Skill.Blades, 3),
                        new WeightedSkill(Skill.Bows, 3),
                        new WeightedSkill(Skill.Entertain, 4),
                        new WeightedSkill(Skill.Leadership, 5),
                        new WeightedSkill(Skill.Logic, 3),
                        new WeightedSkill(Skill.Perception, 2),
                        new WeightedSkill(Skill.Persuade, 5),
                        new WeightedSkill(Skill.Search, 2),
                        new WeightedSkill(Skill.SeekInfo, 4),
                        new WeightedSkill(Skill.Sneak, 1)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnFancyRobe(characterAppearance, CLOTHES_COLOR, DETAIL_COLOR);
        putOnCrown(characterAppearance);
    }

    public static void putOnCrown(CharacterAppearance characterAppearance) {
        characterAppearance.removeOuterHair();
        int spriteOffset =  0xC8;
        if (characterAppearance.hairOnTop()) {
            spriteOffset = 0xA8;
        }
        for (int y = 1; y <= 2; ++y) {
            for (int x = 2; x <= 4; ++x) {
                characterAppearance.setSprite(x, y, new FaceAndClothesSpriteWithBack(spriteOffset + 0x10 * y + x, characterAppearance.getHairColor(), MyColors.LIGHT_GRAY));
            }
        }
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race,0x50, CLOTHES_COLOR, DETAIL_COLOR, appearance.getBackHairOnly());
    }

    @Override
    public Equipment getStartingEquipment() {
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
        return List.of(new Rapier(), new Crown(), new GoldDummyItem(25));
    }
}
