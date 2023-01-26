package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.Equipment;
import model.items.weapons.TrainingBow;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.FaceAndClothesSpriteWithBack;

public class NobleClass extends CharacterClass {
    private static final MyColors CLOTHES_COLOR = MyColors.BLUE;
    private static final MyColors DETAIL_COLOR = MyColors.CYAN;

    protected NobleClass() {
        super("Noble", "N", 7, 5, false, 0,
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

    private static void putOnCrown(CharacterAppearance characterAppearance) {
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
}
