package model.classes.normal;

import model.characters.appearance.CharacterAppearance;
import model.classes.CharacterClass;
import model.classes.Looks;
import model.classes.Skill;
import model.items.Equipment;
import model.items.Item;
import model.items.spells.*;
import model.items.weapons.OldWand;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

import java.util.List;

public class WizardClass extends CharacterClass {
    public WizardClass() {
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
        Looks.putOnPointyHat(characterAppearance, MyColors.BLUE, MyColors.DARK_BLUE, MyColors.DARK_BLUE);
        Looks.putOnRobe(characterAppearance, MyColors.BLUE, MyColors.LIGHT_BLUE);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x40, MyColors.BLUE, race.getColor(),
                appearance.getBackHairOnly(), appearance.getHalfBackHair());
    }

    @Override
    public Equipment getDefaultEquipment() {
        return new Equipment(new OldWand());
    }

    @Override
    public boolean isBackRowCombatant() {
        return true;
    }

    @Override
    protected MyColors getIconColor() {
        return MyColors.BLUE;
    }

    @Override
    protected int getIconNumber() {
        return 0x0D;
    }

    @Override
    public String getHowToLearn() {
        return "Wizards come in two varieties. The respectable ones and hedge wizards. Look for the respectable ones " +
                "in castles. Hedge wizards usually live in hills. Either type should be able to instruct you in wizardry.";
    }

    @Override
    public String getDescription() {
        return "Wizards are the masters of magic. Apart from white magic, wizards are vastly knowledgeable about " +
                "different types of magic. They are also highly intelligent, and are often academics or scholars. " +
                "While not very apt as leaders, they have no difficulty handling social interactions.";
    }

    @Override
    public List<Item> getStartingItems() {
        return List.of(new EntropicBoltSpell(), new EscapeSpell(), new QuickeningSpell(), new BlackPactSpell());
    }
}
