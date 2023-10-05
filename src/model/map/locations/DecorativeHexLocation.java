package model.map.locations;

import model.map.HexLocation;
import view.GameView;
import view.help.HelpDialog;

public abstract class DecorativeHexLocation extends HexLocation {

    public DecorativeHexLocation(String name) {
        super(name);
    }

    @Override
    public boolean isDecoration() {
        return true;
    }

    @Override
    public HelpDialog getHelpDialog(GameView view) {
        return null; // should be unused
    }
}
