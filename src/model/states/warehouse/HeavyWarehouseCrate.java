package model.states.warehouse;

import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class HeavyWarehouseCrate extends WarehouseCrate {

    public static final MyColors COLOR = MyColors.GRAY;
    private static final Sprite32x32 SPRITE = new Sprite32x32(
            "warehousehvycrate", "warehouse.png", 0x11,
            MyColors.BLACK, COLOR, COLOR);

    @Override
    public Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public boolean isImmobile() {
        return true;
    }

    @Override
    public MyColors getColor() {
        return COLOR;
    }
}
