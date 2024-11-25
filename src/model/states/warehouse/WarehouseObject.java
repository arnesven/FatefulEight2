package model.states.warehouse;

import view.MyColors;
import view.sprites.Sprite;

public abstract class WarehouseObject {
    public abstract Sprite getSprite();

    public abstract boolean isImmobile();

    public abstract MyColors getColor();
}
