package model.map.locations;

import model.Model;
import model.actions.DailyAction;
import model.mainstory.honorable.EasternSmithEvent;
import model.map.HillsHex;
import model.map.HillsLocation;
import view.GameView;
import view.MyColors;
import view.ScreenHandler;
import view.help.HelpDialog;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;
import view.subviews.SubView;

import java.awt.*;
import java.util.List;

public class HillsWithHutLocation extends HillsLocation {
    private static final Sprite HUT_SPRITE = HexLocationSprite.make(
            "HillsWithHutLower", 0xDD, MyColors.DARK_BROWN, MyColors.YELLOW, MyColors.BEIGE
    );
    private final HillsHex hillsHex;

    public HillsWithHutLocation() {
        this.hillsHex = new HillsHex(0, 0, 0);
    }

    @Override
    public String getName() {
        return "Hut";
    }

    @Override
    public boolean isDecoration() {
        return false;
    }

    @Override
    public boolean hasDailyActions() {
        return true;
    }

    @Override
    public SubView getImageSubView() {
        return hillsHex.getImageSubView();
    }

    @Override
    public List<DailyAction> getDailyActions(Model model) {
        return List.of(new DailyAction("Visit hut", new EasternSmithEvent(model)));
    }

    @Override
    public HelpDialog getHelpDialog(GameView view) {
        return new HelpDialog(view, "Hut", "A hut in the hills.");
    }

    @Override
    public void drawLowerHalf(ScreenHandler screenHandler, int x, int y) {
        super.drawLowerHalf(screenHandler, x, y);
        screenHandler.register("hillswithhut"+x+""+y, new Point(x, y+2), HUT_SPRITE);
    }
}
