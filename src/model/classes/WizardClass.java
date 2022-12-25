package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.Equipment;
import model.items.weapons.OldWand;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class WizardClass extends CharacterClass {
    protected WizardClass() {
        super("Wizard", "WIZ", 5, 3, false, 24,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.BluntWeapons, 2),
                        new WeightedSkill(Skill.Entertain, 2),
                        new WeightedSkill(Skill.Logic, 5),
                        new WeightedSkill(Skill.MagicBlack, 2),
                        new WeightedSkill(Skill.MagicBlue, 4),
                        new WeightedSkill(Skill.MagicGreen, 4),
                        new WeightedSkill(Skill.MagicRed, 2),
                        new WeightedSkill(Skill.Perception, 4),
                        new WeightedSkill(Skill.Search, 2),
                        new WeightedSkill(Skill.SeekInfo, 4),
                        new WeightedSkill(Skill.SpellCasting, 5)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnPointyHat(characterAppearance, MyColors.BLUE);
        Looks.putOnRobe(characterAppearance, MyColors.BLUE, MyColors.LIGHT_BLUE);
    }

    @Override
    public AvatarSprite getAvatar(Race race) {
        return new AvatarSprite(race, 0x20, MyColors.BLUE);
    }

    @Override
    public Equipment getStartingEquipment() {
        return new Equipment(new OldWand());
    }

    @Override
    public boolean isBackRowCombatant() {
        return true;
    }
}
