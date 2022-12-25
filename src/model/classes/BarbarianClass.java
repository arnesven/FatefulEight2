package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.Equipment;
import model.items.weapons.Hatchet;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class BarbarianClass extends CharacterClass {
    private static final MyColors CLOTHING_COLOR = MyColors.BROWN;

    protected BarbarianClass() {
        super("Barbarian", "BBN", 11, 4, true, 20,
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
    public AvatarSprite getAvatar(Race race) {
        return new AvatarSprite(race, 0x80, CLOTHING_COLOR);
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
