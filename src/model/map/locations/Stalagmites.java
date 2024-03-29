package model.map.locations;

import model.map.CaveHex;
import model.map.HexLocation;
import view.MyColors;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;

public class Stalagmites extends DecorativeHexLocation {

    public Stalagmites() {
        super("Stalagmites");
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("stalagmiteslower", 0x91, MyColors.DARK_GRAY, CaveHex.GROUND_COLOR, MyColors.TAN);
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("stalagmitsupper", 0x81, MyColors.DARK_GRAY, CaveHex.GROUND_COLOR, MyColors.TAN);
    }
}
