package model.map;

import model.map.locations.DecorativeHexLocation;
import view.MyColors;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;

public class DeepWoodsLocation extends DecorativeHexLocation {
    public DeepWoodsLocation() {
        super("Deep Woods");
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("deepwoodslower", 0x50, MyColors.BLACK, MyColors.BROWN, MyColors.DARK_GREEN);
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("deepwoodsupper", 0x40, MyColors.BLACK, MyColors.BROWN, MyColors.DARK_GREEN);
    }
}
