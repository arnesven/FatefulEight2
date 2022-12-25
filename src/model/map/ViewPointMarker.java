package model.map;

import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.sprites.ViewPointMarkerSprite;

import java.awt.*;

public abstract class ViewPointMarker {

    public void updateYourself(ScreenHandler screenHandler, int screenX, int screenY) {
        screenHandler.register(getSpriteName(), new Point(screenX, screenY), getSprite(), 1);
    }

    protected abstract String getSpriteName();

    protected abstract Sprite getSprite();
}
