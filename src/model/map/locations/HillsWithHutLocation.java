package model.map.locations;

import model.Model;
import model.actions.DailyAction;
import model.mainstory.honorable.EasternSmithEvent;
import model.map.HillsLocation;
import model.states.GameState;
import model.states.PickSamuraiSwordState;
import view.GameView;
import view.MyColors;
import view.ScreenHandler;
import view.help.HelpDialog;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.util.List;

public class HillsWithHutLocation extends HillsLocation {
    private static final Sprite HUT_SPRITE = HexLocationSprite.make(
            "HillsWithHutLower", 0xDD, MyColors.DARK_BROWN, MyColors.YELLOW, MyColors.BEIGE
    );

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
