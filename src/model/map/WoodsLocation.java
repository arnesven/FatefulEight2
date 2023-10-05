package model.map;

import model.map.locations.DecorativeHexLocation;
import view.MyColors;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;

public class WoodsLocation extends DecorativeHexLocation {
    private final MyColors snow;

    public WoodsLocation(boolean snow) {
        super("Woods");
        this.snow = snow ? MyColors.WHITE : MyColors.DARK_GREEN;
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("woodslocationlower", 0xB1, MyColors.BLACK, MyColors.BROWN, MyColors.DARK_GREEN, snow);
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("woodslocationupper", 0xA1, MyColors.BLACK, MyColors.BROWN, MyColors.DARK_GREEN, snow);
    }
}
