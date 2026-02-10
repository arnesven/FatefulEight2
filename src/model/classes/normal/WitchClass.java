package model.classes.normal;

import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Looks;
import model.classes.Skill;
import model.classes.WeightedSkill;
import model.items.Item;
import model.items.special.MagicBroom;
import model.items.spells.PoisonGasSpell;
import model.items.spells.TransfigurationSpell;
import model.items.weapons.Dirk;
import model.items.Equipment;
import model.races.Race;
import view.MyColors;
import view.sprites.*;

import java.util.List;

public class WitchClass extends CharacterClass {
    public WitchClass() {
        super("Witch", "WIT", 6, 5, false, 18,
                new WeightedSkill[]{
                        new WeightedSkillPlus(Skill.Blades, 2),
                        new WeightedSkillPlus(Skill.Logic, 4),
                        new WeightedSkill(Skill.MagicBlack, 5),
                        new WeightedSkillPlus(Skill.MagicGreen, 2),
                        new WeightedSkill(Skill.Perception, 3),
                        new WeightedSkill(Skill.Persuade, 2),
                        new WeightedSkillMinus(Skill.Search, 4),
                        new WeightedSkillMinus(Skill.Sneak, 3),
                        new WeightedSkillPlus(Skill.SpellCasting, 3),
                        new WeightedSkillPlus(Skill.Survival, 3),
                        new WeightedSkillMinus(Skill.UnarmedCombat, 2)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnPointyHat(characterAppearance, MyColors.DARK_GRAY, MyColors.DARK_BROWN, MyColors.GOLD);
        Looks.putOnTunic(characterAppearance, MyColors.DARK_GRAY);
    }


    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x40, MyColors.DARK_GRAY, race.getColor(),
                appearance.getBackHairOnly(), appearance.getHalfBackHair());
    }

    @Override
    public Equipment getDefaultEquipment() {
        return new Equipment(new Dirk());
    }

    @Override
    public boolean isBackRowCombatant() {
        return true;
    }

    @Override
    protected MyColors getIconColor() {
        return MyColors.WHITE;
    }

    @Override
    protected int getIconNumber() {
        return 0x09;
    }

    @Override
    public String getHowToLearn() {
        return "Witches can normally be found in woods and swamps. Perhaps one will teach you about witchcraft?";
    }

    @Override
    public String getDescription() {
        return "Witches are magic users who focus on green and black magic. Witches " +
                "can be devilishly clever and quite stealthy to boot. Most witches live alone and must therefore " +
                "learn to protect themselves and live off the land.";
    }

    @Override
    public List<Item> getStartingItems() {
        return List.of(new PoisonGasSpell(), new MagicBroom(), new TransfigurationSpell());
    }
}
