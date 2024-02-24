package model.classes;

import model.characters.appearance.CharacterAppearance;
import model.items.Item;
import model.items.accessories.Circlet;
import model.items.accessories.Tiara;
import model.items.spells.HealingWordSpell;
import model.items.spells.ShiningAegisSpell;
import model.items.weapons.Club;
import model.items.Equipment;
import model.items.weapons.LongStaff;
import model.races.Race;
import view.MyColors;
import view.sprites.AvatarSprite;

import java.util.List;

public class PriestClass extends CharacterClass {
    protected PriestClass() {
        super("Priest", "PRI", 5, 3, false, 16,
                new WeightedSkill[]{
                        new WeightedSkill(Skill.BluntWeapons, 3),
                        new WeightedSkill(Skill.Entertain, 4),
                        new WeightedSkill(Skill.Labor, 3),
                        new WeightedSkill(Skill.Leadership, 4),
                        new WeightedSkill(Skill.Logic, 4),
                        new WeightedSkill(Skill.MagicWhite, 4),
                        new WeightedSkill(Skill.Perception, 2),
                        new WeightedSkill(Skill.Persuade, 4),
                        new WeightedSkill(Skill.SeekInfo, 3),
                        new WeightedSkill(Skill.Sneak, 2),
                        new WeightedSkill(Skill.SpellCasting, 2)
                });
    }

    @Override
    public void putClothesOn(CharacterAppearance characterAppearance) {
        Looks.putOnRobe(characterAppearance, MyColors.LIGHT_YELLOW, MyColors.YELLOW);
    }

    @Override
    public AvatarSprite getAvatar(Race race, CharacterAppearance appearance) {
        return new AvatarSprite(race, 0x27, MyColors.LIGHT_YELLOW, appearance.getNormalHair());
    }

    @Override
    public Equipment getStartingEquipment() {
        return new Equipment(new LongStaff());
    }

    @Override
    public boolean isBackRowCombatant() {
        return true;
    }

    @Override
    protected MyColors getIconColor() {
        return MyColors.LIGHT_YELLOW;
    }

    @Override
    protected int getIconNumber() {
        return 0x12;
    }

    @Override
    public String getHowToLearn() {
        return "Priest will often gladly offer you their teachings. Priests can be found in many places, " +
                "at castles, temples and even wandering the countryside roads, helping those in need";
    }

    @Override
    public String getDescription() {
        return "Priests are monks and healers, normally connected with a religion or faith. They are skilled practitioners " +
                "of white magic and make good leaders. Priests excel in soft skills but are normally not opposed " +
                "to doing manual work.";
    }

    @Override
    public List<Item> getStartingItems() {
        return List.of(new HealingWordSpell(), new ShiningAegisSpell(), new Circlet());
    }
}
