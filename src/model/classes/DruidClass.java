package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.weapons.Club;
import model.items.Equipment;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class DruidClass extends CharacterClass {
    protected DruidClass() {
        super("Druid", "D", 6, 4, false, 20,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Blades, 2),
                        new WeightedSkill(Skill.BluntWeapons, 4),
                        new WeightedSkill(Skill.Endurance, 4),
                        new WeightedSkill(Skill.Labor, 3),
                        new WeightedSkill(Skill.Leadership, 3),
                        new WeightedSkill(Skill.Logic, 3),
                        new WeightedSkill(Skill.MagicGreen, 5),
                        new WeightedSkill(Skill.Sneak, 2),
                        new WeightedSkill(Skill.SpellCasting, 2),
                        new WeightedSkill(Skill.Survival, 5)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnRobe(characterAppearance, MyColors.DARK_GREEN, MyColors.GREEN);
        Looks.putOnHood(characterAppearance, MyColors.DARK_GREEN);
    }

    @Override
    public AvatarSprite getAvatar(Race race) {
        return new AvatarSprite(race, 0x70, MyColors.DARK_GREEN);
    }

    @Override
    public Equipment getStartingEquipment() {
        return new Equipment(new Club());
    }

    @Override
    public boolean isBackRowCombatant() {
        return true;
    }
}
