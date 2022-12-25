package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class ExpensiveRing extends JewelryItem {
    private static final Sprite SPRITE = new ItemSprite(9, 7, MyColors.PEACH, MyColors.GOLD, MyColors.LIGHT_BLUE);

    public ExpensiveRing() {
        super("Expensive Ring", 18);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Leadership, 1), new MyPair<>(Skill.SeekInfo, 1));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new ExpensiveRing();
    }

    @Override
    public int getAP() {
        return 0;
    }
}
