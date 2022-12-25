package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class OrnateRing extends JewelryItem {
    private static final Sprite SPRITE = new ItemSprite(11, 7, MyColors.LIGHT_GRAY, MyColors.GRAY, MyColors.GOLD);

    public OrnateRing() {
        super("Ornate Ring", 12);
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.MagicWhite, 1));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new OrnateRing();
    }

    @Override
    public int getAP() {
        return 0;
    }
}
