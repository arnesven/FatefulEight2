package model.items.clothing;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class RingMail extends Clothing {
    private static final Sprite SPRITE = new ItemSprite(4, 2);

    public RingMail() {
        super("Ring Mail", 26, 3, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new RingMail();
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Sneak, -1));
    }
}
