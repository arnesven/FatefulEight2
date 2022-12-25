package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class EmeraldRing extends JewelryItem {
    private static final Sprite SPRITE = new ItemSprite(6, 9, MyColors.BEIGE, MyColors.GREEN, MyColors.LIGHT_GREEN);

    public EmeraldRing() {
        super("Emerald Ring", 16);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Bows, 2));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new EmeraldRing();
    }

    @Override
    public int getAP() {
        return 0;
    }
}
