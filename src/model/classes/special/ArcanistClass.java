package model.classes.special;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.classes.Skill;
import model.classes.WeightedSkill;
import model.items.Equipment;
import model.items.weapons.OldWand;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class ArcanistClass extends SpecialCharacterClass {
    public ArcanistClass() {
        super("Arcanist", "ARC", 6, 2, false, 0,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Academics, 5),
                        new WeightedSkill(Skill.Entertain, 3),
                        new WeightedSkill(Skill.Labor, 4),
                        new WeightedSkill(Skill.Logic, 5),
                        new WeightedSkill(Skill.MagicBlue, 5),
                        new WeightedSkill(Skill.MagicGreen, 2),
                        new WeightedSkill(Skill.MagicWhite, 4),
                        new WeightedSkill(Skill.Perception, 5),
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
        return new AvatarSprite(race, 0x160, MyColors.LIGHT_PINK, race.getColor(), appearance.getNormalHair(), appearance.getFullBackHair());
    }

    @Override
    public Equipment getDefaultEquipment() {
        return new Equipment(new OldWand());
    }

    @Override
    public boolean isBackRowCombatant() {
        return true;
    }
}
