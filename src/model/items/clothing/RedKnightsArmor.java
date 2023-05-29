package model.items.clothing;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class RedKnightsArmor extends FullPlateArmor {
    private static final Sprite SPRITE = new RedKnightsArmorSprite();

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new RedKnightsArmor();
    }

    private static class RedKnightsArmorSprite extends ItemSprite {
        public RedKnightsArmorSprite() {
            super(9, 2);
            setColor1(MyColors.RED);
            setColor3(MyColors.DARK_RED);
        }
    }
}
