package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.weapons.Club;
import model.items.Equipment;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class BardClass extends CharacterClass {
    protected BardClass() {
        super("Bard", "BRD", 6, 5, false, 18,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Acrobatics, 3),
                        new WeightedSkill(Skill.BluntWeapons, 2),
                        new WeightedSkill(Skill.Entertain, 5),
                        new WeightedSkill(Skill.Leadership, 3),
                        new WeightedSkill(Skill.Logic, 3),
                        new WeightedSkill(Skill.Perception, 4),
                        new WeightedSkill(Skill.Persuade, 6),
                        new WeightedSkill(Skill.Security, 3),
                        new WeightedSkill(Skill.SeekInfo, 4),
                        new WeightedSkill(Skill.Sneak, 4)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnLooseShirt(characterAppearance, MyColors.PURPLE);
        Looks.putOnCap(characterAppearance, MyColors.PURPLE);
    }

    @Override
    public AvatarSprite getAvatar(Race race) {
        return new AvatarSprite(race, 0x30, MyColors.PURPLE);
    }

    @Override
    public Equipment getStartingEquipment() {
        return new Equipment(new Club());
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
