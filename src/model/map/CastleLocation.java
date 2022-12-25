package model.map;

import view.MyColors;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;

import java.awt.*;

public class CastleLocation extends HexLocation implements LordLocation {
    // TODO: Add imagesubview
    private final String lordName;
    private MyColors castleColor;

    public CastleLocation(String castleName, MyColors castleColor, String lordName) {
        super(castleName);
        this.castleColor = castleColor;
        this.lordName = lordName;
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("castleupper", 0xC0, MyColors.BLACK, MyColors.LIGHT_GRAY, this.castleColor);
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("castlelower", 0xD0, MyColors.BLACK, MyColors.LIGHT_GRAY, this.castleColor);
    }

    @Override
    public boolean isDecoration() {
        return false;
    }

    @Override
    public boolean hasLodging() {
        return true;
    }

    @Override
    public String getLordName() {
        return lordName;
    }

    @Override
    public String getPlaceName() {
        return getName();
    }

    @Override
    public boolean givesQuests() {
        return true;
    }
}
