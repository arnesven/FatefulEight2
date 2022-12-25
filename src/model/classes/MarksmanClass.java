package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.Equipment;
import model.items.weapons.TrainingBow;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.FaceAndClothesSpriteWithBack;

public class MarksmanClass extends CharacterClass {
    private static final MyColors ARMOR_COLOR = MyColors.BROWN;

    protected MarksmanClass() {
        super("Marksman", "MAR", 8, 6, false, 23,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Bows, 5),
                        new WeightedSkill(Skill.Endurance, 2),
                        new WeightedSkill(Skill.Leadership, 2),
                        new WeightedSkill(Skill.Perception, 5),
                        new WeightedSkill(Skill.Polearms, 2),
                        new WeightedSkill(Skill.Search, 4),
                        new WeightedSkill(Skill.Sneak, 4),
                        new WeightedSkill(Skill.Survival, 3)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnTunic(characterAppearance, MyColors.DARK_GRAY);
        Looks.putOnLightArmor(characterAppearance, ARMOR_COLOR, MyColors.DARK_GRAY);
        putOnLeatherCap(characterAppearance, ARMOR_COLOR);
    }

    private void putOnLeatherCap(CharacterAppearance characterAppearance, MyColors color) {
        for (int y = 1; y <= 2; ++y) {
            for (int x = 2; x <= 4; ++x) {
                characterAppearance.setSprite(x, y, new FaceAndClothesSpriteWithBack(0xAB + 0x10 * y + x, characterAppearance.getHairColor(), color));
            }
        }
    }

    @Override
    public AvatarSprite getAvatar(Race race) {
        return new AvatarSprite(race, 0x07, ARMOR_COLOR);
    }

    @Override
    public Equipment getStartingEquipment() {
        return new Equipment(new TrainingBow());
    }

    @Override
    public boolean isBackRowCombatant() {
        return true;
    }
}
