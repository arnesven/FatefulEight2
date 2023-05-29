package model.items;

import model.items.accessories.FullHelm;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class RedKnightsHelm extends FullHelm {
    private static final Sprite SPRITE = new RedKnightsHelmSprite();

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new RedKnightsHelm();
    }

    private static class RedKnightsHelmSprite extends ItemSprite {
        public RedKnightsHelmSprite() {
            super(11, 9, MyColors.DARK_RED, MyColors.DARK_GRAY);
            setColor1(MyColors.RED);
        }
    }
}
