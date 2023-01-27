package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.weapons.Dirk;
import model.items.Equipment;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class ThiefClass extends CharacterClass {
    protected ThiefClass() {
        super("Thief", "T", 6, 6, false, 0,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Acrobatics, 4),
                        new WeightedSkill(Skill.Blades, 3),
                        new WeightedSkill(Skill.BluntWeapons, 1),
                        new WeightedSkill(Skill.Entertain, 2),
                        new WeightedSkill(Skill.Logic, 3),
                        new WeightedSkill(Skill.Perception, 5),
                        new WeightedSkill(Skill.Persuade, 4),
                        new WeightedSkill(Skill.Search, 5),
                        new WeightedSkill(Skill.Security, 5),
                        new WeightedSkill(Skill.SeekInfo, 4),
                        new WeightedSkill(Skill.Sneak, 5)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnTunic(characterAppearance, MyColors.BROWN);
        Looks.putOnHood(characterAppearance, MyColors.BROWN);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x00, MyColors.BROWN, appearance.getFacialOnly());
    }

    @Override
    public Equipment getStartingEquipment() {
        return new Equipment(new Dirk());
    }

    @Override
    public boolean isBackRowCombatant() {
        return false;
    }

    @Override
    public void manipulateAvatar(CharacterAppearance appearance, Race race) {
        super.finalizeLook(appearance);
        if (race.isShort()) {
            appearance.getFacialOnly().shiftUpPx(-2);
        }
    }
}
