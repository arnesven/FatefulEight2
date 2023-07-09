package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.Equipment;
import model.items.weapons.OldWand;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class ArcanistClass extends CharacterClass {
    protected ArcanistClass() {
        super("Arcanist", "ARC", 6, 2, false, 0,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Entertain, 3),
                        new WeightedSkill(Skill.Labor, 4),
                        new WeightedSkill(Skill.Logic, 5),
                        new WeightedSkill(Skill.MagicBlue, 5),
                        new WeightedSkill(Skill.MagicGreen, 2),
                        new WeightedSkill(Skill.MagicWhite, 4),
                        new WeightedSkill(Skill.Perception, 5),
                        new WeightedSkill(Skill.Search, 5),
                        new WeightedSkill(Skill.Sneak, 3),
                        new WeightedSkill(Skill.SpellCasting, 3),
        });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnRobe(characterAppearance, MyColors.LIGHT_PINK, MyColors.PINK);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return null;
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
