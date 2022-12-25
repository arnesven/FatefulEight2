package view.sprites;

import view.MyColors;

import java.util.ArrayList;
import java.util.List;

public abstract class PortraitSprite extends Sprite8x8 {
    public PortraitSprite(String name, String mapPath, int number, List<Sprite> layers) {
        super(name, mapPath, number, layers);
    }

    public PortraitSprite(String name, String mapPath, int number) {
        this(name, mapPath, number, new ArrayList<>());
    }

    public abstract void setSkinColor(MyColors color);
}
