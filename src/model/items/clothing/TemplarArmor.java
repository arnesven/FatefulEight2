package model.items.clothing;

import model.classes.Skill;
import model.items.Item;
import model.items.Prevalence;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class TemplarArmor extends HeavyArmorClothing {

    private static final Sprite SPRITE = new ItemSprite(9, 3, MyColors.WHITE, MyColors.LIGHT_GRAY, MyColors.RED);

    public TemplarArmor() {
        super("Templar Armor", 45, 4);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.SeekInfo, 2));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new TemplarArmor();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
