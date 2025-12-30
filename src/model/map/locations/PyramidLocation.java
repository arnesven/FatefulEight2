package model.map.locations;

import model.map.HexLocation;
import view.GameView;
import view.MyColors;
import view.help.HelpDialog;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;

public class PyramidLocation extends HexLocation {
    public PyramidLocation(String name) {
        super(name + " Pyramid");
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("junglepyramidupper", 0x16C, MyColors.BLACK, MyColors.GRAY, MyColors.BROWN, MyColors.DARK_GREEN);
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("junglepyramidlower", 0x17C, MyColors.BLACK, MyColors.GRAY, MyColors.BROWN, MyColors.DARK_GREEN);
    }


    @Override
    public boolean isDecoration() {
        return false;
    }

    @Override
    public HelpDialog getHelpDialog(GameView view) {
        return null;
    }
}
