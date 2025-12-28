package model.map.locations;

import model.map.HexLocation;
import view.GameView;
import view.MyColors;
import view.help.HelpDialog;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;

public class GrassCorner extends HexLocation {

    public GrassCorner() {
        super("Island");
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
        return true;
    }

    @Override
    public HelpDialog getHelpDialog(GameView view) {
        return new HelpDialog(view, "Island", "An island...");
    }

}
