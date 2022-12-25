package model.map;

import view.sprites.Sprite;
import view.sprites.ViewPointMarkerSprite;

public class HexCursorMarker extends ViewPointMarker {
    private static final Sprite viewPointSprite = new ViewPointMarkerSprite();
    @Override
    protected String getSpriteName() {
        return "viewpointsprite";
    }

    @Override
    protected Sprite getSprite() {
       return viewPointSprite;
    }
}
