package model.map.locations;

import model.map.HexLocation;
import view.GameView;
import view.MyColors;
import view.help.HelpDialog;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;
import view.subviews.ImageSubView;
import view.subviews.SubView;

public class GrassCorner extends HexLocation {

    private final ImageSubView subView;

    public GrassCorner() {
        super("Island");
        subView = new ImageSubView("island", "ISLAND", "You are on an island...", true);
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("grasscornerupper", 0x181, MyColors.GREEN, MyColors.GRAY, MyColors.DARK_GREEN, MyColors.GREEN);
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("grasscornerlower", 0x191, MyColors.GREEN, MyColors.GRAY, MyColors.DARK_GREEN, MyColors.GREEN);
    }

    @Override
    public boolean isDecoration() {
        return false;
    }

    @Override
    public HelpDialog getHelpDialog(GameView view) {
        return new HelpDialog(view, "Island", "An island...");
    }

    @Override
    public SubView getImageSubView() {
        return subView;
    }

    @Override
    public boolean showNameOnMap() {
        return false;
    }
}
