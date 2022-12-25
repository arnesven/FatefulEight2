package model.map;

import view.MyColors;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;

public class FieldsLocation extends HexLocation {
    public FieldsLocation() {
        super("fielddecoration");
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("fieldslocationlower", 0x31, MyColors.BLACK, MyColors.BROWN, MyColors.GREEN);
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("fieldslocationupper", 0x21, MyColors.BLACK, MyColors.BROWN, MyColors.GREEN);
    }

    @Override
    public boolean isDecoration() {
        return true;
    }
}
