package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.Equipment;
import model.items.weapons.Hatchet;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class ForesterClass extends CharacterClass {
    protected ForesterClass() {
        super("Forester", "F", 10, 4, false, 0,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Acrobatics, 2),
                        new WeightedSkill(Skill.Axes, 5),
                        new WeightedSkill(Skill.BluntWeapons, 1),
                        new WeightedSkill(Skill.Bows, 3),
                        new WeightedSkill(Skill.Endurance, 5),
                        new WeightedSkill(Skill.Entertain, 1),
                        new WeightedSkill(Skill.Labor, 5),
                        new WeightedSkill(Skill.Leadership, 4),
                        new WeightedSkill(Skill.Perception, 2),
                        new WeightedSkill(Skill.Search, 4),
                        new WeightedSkill(Skill.Survival, 5)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnLooseShirt(characterAppearance, MyColors.RED);
        Looks.putOnCap(characterAppearance, MyColors.DARK_GREEN);
    }

    @Override
    public AvatarSprite getAvatar(Race race) {
        return new AvatarSprite(race, 0x30, MyColors.RED, MyColors.DARK_GREEN);
    }

    @Override
    public Equipment getStartingEquipment() {
        return new Equipment(new Hatchet());
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }

    @Override
    public void finalizeLook(CharacterAppearance appearance) {
        Looks.finalizeCap(appearance);
    }
}
