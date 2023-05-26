package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.weapons.Club;
import model.items.Equipment;
import model.races.Race;
import view.MyColors;
import view.sprites.*;

public class BlackKnightClass extends CharacterClass {
    protected BlackKnightClass() {
        super("Black Knight", "BKN", 8, 6, true, 16,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Blades, 3),
                        new WeightedSkill(Skill.BluntWeapons, 5),
                        new WeightedSkill(Skill.Endurance, 2),
                        new WeightedSkill(Skill.Labor, 2),
                        new WeightedSkill(Skill.Leadership, 2),
                        new WeightedSkill(Skill.Perception, 4),
                        new WeightedSkill(Skill.Persuade, 2),
                        new WeightedSkill(Skill.Search, 1),
                        new WeightedSkill(Skill.Survival, 2)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnArmor(characterAppearance, MyColors.DARK_GRAY, MyColors.DARK_GREEN);
        putOnKnightsHelm(characterAppearance);
    }


    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x40, MyColors.DARK_GRAY, CharacterAppearance.noHair());
    }

    @Override
    public Equipment getStartingEquipment() {
        return new Equipment(new Club());
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }

    private static void putOnKnightsHelm(CharacterAppearance characterAppearance) {
        characterAppearance.removeOuterHair();
        for (int y = 1; y <= 4; ++y) {
            characterAppearance.setSprite(1, y, new FilledBlockSprite(MyColors.BLACK));
            characterAppearance.setSprite(5, y, new FilledBlockSprite(MyColors.BLACK));
        }
        for (int y = 0; y <= 5; ++y) {
            for (int x = 2; x <= 4; ++x) {
                if (y == 4) {
                    characterAppearance.setSprite(x, y, new ClothesSpriteWithBack(0x20 + x + 2, MyColors.DARK_GRAY, MyColors.DARK_GREEN));
                } else {
                    characterAppearance.setSprite(x, y, new ClothesSpriteWithBack(0x10 * y + x + 2, MyColors.DARK_GRAY, MyColors.DARK_GREEN));
                }
            }
        }
    }

    @Override
    public void finalizeLook(CharacterAppearance appearance) {
        // remove ears
        appearance.setSprite(1, 3, new FilledBlockSprite(MyColors.BLACK));
        appearance.setSprite(5, 3, new FilledBlockSprite(MyColors.BLACK));
    }

    @Override
    public boolean showFacialHair() {
        return false;
    }

    @Override
    public boolean coversEars() {
        return true;
    }
}
