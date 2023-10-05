package model.map;

import model.map.locations.DecorativeHexLocation;
import view.MyColors;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;

public class JungleLocation extends DecorativeHexLocation {

    public JungleLocation() {
        super("Jungle");
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("junglelower", 0xD1, MyColors.BLACK, MyColors.BROWN, MyColors.DARK_GREEN);
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("jungleupper", 0xC1, MyColors.BLACK, MyColors.BROWN, MyColors.DARK_GREEN);
    }

}
