package model.states.warehouse;

import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.CrateAndAvatarSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class WarehouseCrate extends WarehouseObject {

    public static final MyColors COLOR = MyColors.GOLD;
    private static final Sprite32x32 SPRITE = new Sprite32x32(
            "warehousecrate", "warehouse.png", 0x11,
            MyColors.DARK_GRAY, COLOR, COLOR);

    public static CrateAndAvatarSprite makeCombinedSprite(Sprite avatarSprite, int col, int row, int width, int height) {
        return new CrateAndAvatarSprite(avatarSprite, COLOR, COLOR, col, row, width, height);
    }

    @Override
    public Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public boolean isImmobile() {
        return false;
    }

    public MyColors getColor() {
        return COLOR;
    }
}
