package model.items.clothing;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import util.MyRandom;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class WarmCape extends Clothing {
    private final ItemSprite sprite;

    public WarmCape() {
        super("Warm Cape", 12, 0, false);
        this.sprite = new ItemSprite(10, 2, MyColors.DARK_RED, MyColors.DARK_BROWN,
                MyRandom.sample(List.of(MyColors.BEIGE, MyColors.TAN, MyColors.LIGHT_YELLOW)));
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Survival, 1), new MyPair<>(Skill.Endurance, 1));
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public Item copy() {
        return new WarmCape();
    }

    @Override
    public int getWeight() {
        return 1000;
    }
}
