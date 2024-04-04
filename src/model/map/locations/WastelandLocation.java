package model.map.locations;

import view.MyColors;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;

public class WastelandLocation extends DecorativeHexLocation {
    public WastelandLocation() {
        super("wastelandlocation");
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("wastelandlower", 0x51, MyColors.BLACK, MyColors.BROWN, MyColors.TAN);
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("wastelandupper", 0x41, MyColors.BLACK, MyColors.BROWN, MyColors.TAN);
    }
}
