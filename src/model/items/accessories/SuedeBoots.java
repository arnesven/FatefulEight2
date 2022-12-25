package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class SuedeBoots extends ShoesItem {
    private static final Sprite SPRITE = new ItemSprite(7, 10, MyColors.DARK_GREEN, MyColors.GREEN);

    public SuedeBoots() {
        super("Suede Boots", 12);
    }

    @Override
    public int getSpeedModifier() {
        return 1;
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Survival, 1));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new SuedeBoots();
    }

    @Override
    public int getAP() {
        return 0;
    }
}
