package model.map;

import view.MyColors;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;

public class JungleLocation extends HexLocation {

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


    @Override
    public boolean isDecoration() {
        return true;
    }
}
