package model.items.clothing;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class QuiltedArmor extends Clothing {
    private static final Sprite SPRITE = new ItemSprite(15, 1);

    public QuiltedArmor() {
        super("Quilted Armor", 16, 1, false);
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
        return new QuiltedArmor();
    }

    @Override
    public int getWeight() {
        return 2000;
    }
}
