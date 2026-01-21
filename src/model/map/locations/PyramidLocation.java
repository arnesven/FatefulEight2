package model.map.locations;

import model.Model;
import model.map.HexLocation;
import model.map.JungleHex;
import model.states.DailyEventState;
import view.GameView;
import view.MyColors;
import view.help.HelpDialog;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;
import view.subviews.ImageSubView;
import view.subviews.SubView;

public class PyramidLocation extends HexLocation {
    private final ImageSubView subView;
    private final JungleHex jungleHex;

    public PyramidLocation(String name) {
        super(name + " Pyramid");
        subView = new ImageSubView("pyramid", getName().toUpperCase(), "You are at the " + getName(), true);
        this.jungleHex = new JungleHex(0, 0, 0);
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
    public SubView getImageSubView(Model model) {
        return subView;
    }

    @Override
    public boolean isDecoration() {
        return false;
    }

    @Override
    public DailyEventState generateEvent(Model model) {
        return jungleHex.generateEvent(model);
    }

    @Override
    public HelpDialog getHelpDialog(GameView view) {
        return new PyramidHelpDialog(view);
    }

    private class PyramidHelpDialog extends HelpDialog {
        public PyramidHelpDialog(GameView view) {
            super(view, "Pyramid", "The " + getName() + ".");
        }
    }
}
