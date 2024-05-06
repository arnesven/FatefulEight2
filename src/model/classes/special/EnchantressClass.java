package model.classes.special;

import model.characters.appearance.CharacterAppearance;
import model.classes.Looks;
import model.classes.Skill;
import model.items.Equipment;
import model.items.clothing.EnchantressDress;
import model.items.weapons.UnarmedCombatWeapon;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

public class EnchantressClass extends SpecialCharacterClass {
    public EnchantressClass() {
        super("Enchantress", "ENC", 6, 6, false, 0,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.Acrobatics, 3),
                        new WeightedSkill(Skill.Bows, 2),
                        new WeightedSkill(Skill.Entertain, 5),
                        new WeightedSkill(Skill.Labor, 2),
                        new WeightedSkill(Skill.Logic, 2),
                        new WeightedSkill(Skill.MagicBlue, 5),
                        new WeightedSkill(Skill.MagicGreen, 5),
                        new WeightedSkill(Skill.MagicWhite, 5),
                        new WeightedSkill(Skill.Perception, 4),
                        new WeightedSkill(Skill.Persuade, 5),
                        new WeightedSkill(Skill.Security, 3),
                        new WeightedSkill(Skill.SeekInfo, 4),
                        new WeightedSkill(Skill.Sneak, 4),
                        new WeightedSkill(Skill.Survival, 4),
                        new WeightedSkill(Skill.SpellCasting, 3)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnFancyDress(characterAppearance, MyColors.GREEN, MyColors.GOLD);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x148, MyColors.GOLD, MyColors.GREEN,
                appearance.getNormalHair(), appearance.getFullBackHair());
    }

    @Override
    public Equipment getStartingEquipment() {
        return new Equipment(new UnarmedCombatWeapon(), new EnchantressDress(), null);
    }

    @Override
    public boolean isBackRowCombatant() {
        return true;
    }
}
