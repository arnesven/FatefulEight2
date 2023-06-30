package view.sprites;

import view.MyColors;
import view.subviews.TownSubView;

import java.util.ArrayList;

public class LibrarySprite extends Sprite {
    public LibrarySprite() {
        super("librarysprite", "world_foreground.png", 3, 8, 64, 32, new ArrayList<>());
        setColor1(TownSubView.STREET_COLOR);
        setColor2(TownSubView.PATH_COLOR);
        setColor3(MyColors.BROWN);
        setColor4(MyColors.BEIGE);
    }
}
