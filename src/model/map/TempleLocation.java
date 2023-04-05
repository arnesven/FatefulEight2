package model.map;

import util.MyPair;
import view.MyColors;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;
import view.subviews.DailyActionMenu;
import view.subviews.ImageSubView;
import view.subviews.SubView;

import java.awt.*;

public class TempleLocation extends HexLocation {
    private final ImageSubView subView;

    public TempleLocation(String templeName) {
        super("Temple of " + templeName);
        subView = new ImageSubView("temple", "TEMPLE", "Temple of " + templeName, true);
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("templeupper", 0xE0, MyColors.BLACK, MyColors.WHITE, MyColors.PEACH);
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("templelower", 0xF0, MyColors.BLACK, MyColors.WHITE, MyColors.PEACH);
    }

    @Override
    public SubView getImageSubView() {
        return subView;
    }

    @Override
    public boolean isDecoration() {
        return false;
    }

    @Override
    public boolean hasLodging() {
        return true;
    }

    @Override
    public MyPair<Point, Integer> getDailyActionMenuAnchor() {
        return DailyActionMenu.LOWER_LEFT_CORNER;
    }
}
