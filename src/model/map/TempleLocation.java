package model.map;

import view.MyColors;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;

public class TempleLocation extends HexLocation {
    // TODO: Add imagesubview
    public TempleLocation(String templeName) {
        super("Temple of " + templeName);
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("templeupper", 0xE0, MyColors.BLACK, MyColors.WHITE, MyColors.PEACH);
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("templelower", 0xF0, MyColors.BLACK, MyColors.WHITE, MyColors.PEACH);
    }

    @Override
    public boolean isDecoration() {
        return false;
    }

    @Override
    public boolean hasLodging() {
        return true;
    }
}
