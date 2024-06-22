package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.ArrayList;
import java.util.List;

public class ComfyShoes extends ShoesItem {

    private static final Sprite SPRITE =  new ItemSprite(0, 0xA, MyColors.TAN, MyColors.DARK_GRAY);

    public ComfyShoes() {
        super("Comfy Shoes", 8);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new ComfyShoes();
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        List<MyPair<Skill, Integer>> result = new ArrayList<>(super.getSkillBonuses());
        result.add(new MyPair<>(Skill.Sneak, 1));
        return result;
    }

    @Override
    public int getAP() {
        return 0;
    }
}
