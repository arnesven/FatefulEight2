package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.Equipment;
import model.items.weapons.Hatchet;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.ClothesSpriteWithBack;

public class MinerClass extends CharacterClass {
    protected MinerClass() {
        super("Miner", "MIN", 12, 3, true, 14,
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
        return new AvatarSprite(race, 0x77, MyColors.DARK_GRAY, MyColors.LIGHT_YELLOW, appearance.getBackHairOnly());
    }

    @Override
    public Equipment getStartingEquipment() {
        return new Equipment(new Hatchet());
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }
}
