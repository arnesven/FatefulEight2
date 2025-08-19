package model.map.objects;

import model.Model;
import model.map.WorldHex;
import util.MyPair;
import view.ScreenHandler;
import view.sprites.Sprite;

import java.awt.*;
import java.util.List;

public abstract class MapFilter {
    public abstract List<MyPair<Point, Sprite>> getObjects(Model model);

    public abstract void drawLegend(ScreenHandler screenHandler, int x, int y);

    public void drawSpecial(Model model, WorldHex[][] hexes, int x, int y, int screenX, int screenY) {

    }
}
