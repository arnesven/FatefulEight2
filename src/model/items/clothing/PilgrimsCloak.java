package model.items.clothing;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class PilgrimsCloak extends Clothing {
    private static final Sprite SPRITE = new ItemSprite(12, 2, MyColors.LIGHT_GRAY, MyColors.GRAY, MyColors.WHITE);

    public PilgrimsCloak() {
        super("Pilgrim's Cloak", 20, 1, false);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Sneak, 1));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new PilgrimsCloak();
    }
}
