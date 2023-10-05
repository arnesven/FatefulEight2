package model.map;

import model.map.locations.DecorativeHexLocation;
import view.MyColors;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;

import java.awt.*;

public class MountainLocation extends DecorativeHexLocation {

    public MountainLocation() {
        super("mountainlocation");
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("mountainlocationlower", 0xB0, MyColors.BLACK, MyColors.WHITE, MyColors.GRAY);
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("mountainlocationupper", 0xA0, MyColors.BLACK, MyColors.WHITE, MyColors.GRAY);
    }
}
