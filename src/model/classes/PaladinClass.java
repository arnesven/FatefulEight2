package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.weapons.Club;
import model.items.Equipment;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class PaladinClass extends CharacterClass {

    private static final MyColors UNDERSHIRT_COLOR = MyColors.GRAY;
    private static final MyColors ARMOR_COLOR = MyColors.WHITE;

    protected PaladinClass() {
        super("Paladin", "PAL", 10, 4, true, 16,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Blades, 4),
                        new WeightedSkill(Skill.BluntWeapons, 4),
                        new WeightedSkill(Skill.Endurance, 3),
                        new WeightedSkill(Skill.Labor, 1),
                        new WeightedSkill(Skill.Leadership, 4),
                        new WeightedSkill(Skill.MagicWhite, 3),
                        new WeightedSkill(Skill.Perception, 2),
                        new WeightedSkill(Skill.Polearms, 4),
                        new WeightedSkill(Skill.Survival, 2)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnArmor(characterAppearance, ARMOR_COLOR, UNDERSHIRT_COLOR);
    }

    @Override
    public AvatarSprite getAvatar(Race race) {
        return new AvatarSprite(race, 0x10, MyColors.WHITE);
    }

    @Override
    public Equipment getStartingEquipment() {
        return new Equipment(new Club());
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }
}
