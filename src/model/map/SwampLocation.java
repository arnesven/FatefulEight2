package model.map;

import model.map.locations.DecorativeHexLocation;
import view.MyColors;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;

public class SwampLocation extends DecorativeHexLocation {
    public SwampLocation() {
        super("swampdecoration");
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("swampupper", 0x51, MyColors.DARK_BROWN, MyColors.DARK_GREEN, MyColors.TAN);
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("swamplower", 0x41, MyColors.DARK_BROWN, MyColors.DARK_GREEN, MyColors.TAN);
    }
}
