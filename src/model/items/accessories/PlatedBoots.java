package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class PlatedBoots extends ShoesItem {
    private static final Sprite SPRITE = new ItemSprite(8, 10, MyColors.GRAY, MyColors.LIGHT_GRAY);

    public PlatedBoots() {
        super("Plated Boots", 22);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Sneak, -1));
    }

    @Override
    public boolean isHeavy() {
        return true;
    }

    @Override
    public Item copy() {
        return new PlatedBoots();
    }

    @Override
    public int getAP() {
        return 2;
    }
}
