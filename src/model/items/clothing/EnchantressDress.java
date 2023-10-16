package model.items.clothing;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class EnchantressDress extends Clothing {
    private static final Sprite SPRITE = new DressSprite();

    public EnchantressDress() {
        super("Fancy Dress", 0, 0, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new EnchantressDress();
    }

    private static class DressSprite extends ItemSprite {
        public DressSprite() {
            super(15, 3);
            setColor1(MyColors.RED);
            setColor2(MyColors.GREEN);
            setColor3(MyColors.GOLD);
            setColor4(MyColors.DARK_GREEN);
        }
    }

    @Override
    public int getWeight() {
        return 500;
    }
}
