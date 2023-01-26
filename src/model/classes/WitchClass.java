package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.weapons.Dirk;
import model.items.Equipment;
import model.races.Race;
import view.MyColors;
import view.sprites.*;

public class WitchClass extends CharacterClass {
    protected WitchClass() {
        super("Witch", "WIT", 6, 5, false, 28,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Blades, 2),
                        new WeightedSkill(Skill.Logic, 4),
                        new WeightedSkill(Skill.MagicBlack, 5),
                        new WeightedSkill(Skill.MagicGreen, 2),
                        new WeightedSkill(Skill.Perception, 3),
                        new WeightedSkill(Skill.Persuade, 2),
                        new WeightedSkill(Skill.Search, 3),
                        new WeightedSkill(Skill.Sneak, 2),
                        new WeightedSkill(Skill.SpellCasting, 3),
                        new WeightedSkill(Skill.Survival, 3),
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnPointyHat(characterAppearance, MyColors.DARK_GRAY);
        Looks.putOnTunic(characterAppearance, MyColors.DARK_GRAY);
    }


    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x20, MyColors.DARK_GRAY, appearance.getBackHairOnly());
    }

    @Override
    public Equipment getStartingEquipment() {
        return new Equipment(new Dirk());
    }

    @Override
    public boolean isBackRowCombatant() {
        return true;
    }
}
