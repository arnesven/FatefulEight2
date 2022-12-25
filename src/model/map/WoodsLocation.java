package model.map;

import view.MyColors;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;

public class WoodsLocation extends HexLocation {
    public WoodsLocation() {
        super("Woods");
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("woodslocationlower", 0x50, MyColors.BLACK, MyColors.BROWN, MyColors.DARK_GREEN);
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("woodslocationupper", 0x40, MyColors.BLACK, MyColors.BROWN, MyColors.DARK_GREEN);
    }

    @Override
    public boolean isDecoration() {
        return true;
    }
}
