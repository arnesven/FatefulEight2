package model.items.clothing;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class TradersDoublet extends Clothing {
    private static final Sprite SPRITE = new ItemSprite(0, 8, MyColors.BROWN, MyColors.YELLOW, MyColors.GOLD);

    public TradersDoublet() {
        super("Trader's Doublet", 16, 0, false);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Labor, 1), new MyPair<>(Skill.Mercantile, 1),
                new MyPair<>(Skill.Persuade, 1));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getWeight() {
        return 1000;
    }

    @Override
    public Item copy() {
        return new TradersDoublet();
    }
}
