package model.map;

import view.MyColors;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;

public class HillsLocation extends HexLocation {
    private final MyColors hillColor;

    public HillsLocation(MyColors hillColor) {
        super("hillslocation");
        this.hillColor = hillColor;
    }

    public HillsLocation() {
        this(MyColors.GREEN);
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("hillslocationlower", 0x70, MyColors.BLACK, hillColor, MyColors.BLACK);
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("hillslocationupper", 0x60, MyColors.BLACK, hillColor, MyColors.BLACK);
    }

    @Override
    public boolean isDecoration() {
        return true;
    }
}
