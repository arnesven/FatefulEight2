package model.states.warehouse;

import view.MyColors;
import view.sprites.CrateAndAvatarSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class SpecialWarehouseCrate extends WarehouseObject {
    public static final MyColors COLOR = MyColors.BEIGE;

    private static final Sprite32x32 SPRITE = new Sprite32x32(
            "warehousecrate", "warehouse.png", 0x11,
            MyColors.DARK_GRAY, COLOR, MyColors.GOLD);

    @Override
    public Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public boolean isImmobile() {
        return false;
    }

    @Override
    public MyColors getColor() {
        return COLOR;
    }

    public static CrateAndAvatarSprite makeCombinedSprite(Sprite avatarSprite, int col, int row, int width, int height) {
        return new CrateAndAvatarSprite(avatarSprite, COLOR, MyColors.GOLD, col, row, width, height);
    }
}
