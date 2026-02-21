package model.items.accessories;

import model.classes.Skill;
import model.items.Item;
import util.MyPair;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.List;

public class OnyxRing extends JewelryItem {
    private static final Sprite SPRITE = new ItemSprite(6, 9, MyColors.DARK_GRAY, MyColors.DARK_PURPLE, MyColors.BLACK);

    public OnyxRing() {
        super("Onyx Ring", 14);
    }

    @Override
    public int getAP() {
        return 0;
    }

    @Override
    public List<MyPair<Skill, Integer>> getSkillBonuses() {
        return List.of(new MyPair<>(Skill.Academics, 1), new MyPair<>(Skill.UnarmedCombat, 1));
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new OnyxRing();
    }
}
