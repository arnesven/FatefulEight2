package model.map.objects;

import model.Model;
import model.actions.DailyAction;
import model.actions.EnterCavesAction;
import model.actions.ExitCavesAction;
import model.map.CaveHex;
import view.MyColors;
import view.ScreenHandler;
import view.help.HelpDialog;
import view.help.SpecificTerrainHelpDialog;
import view.help.TerrainHelpChapter;
import view.sprites.HexLocationSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.util.List;

public class UnderworldEntrance extends MapObject {
    private static final Sprite LOWER_SPRITE = HexLocationSprite.make("cavelower", 0x193, MyColors.BLACK, MyColors.WHITE, MyColors.RED);
    private static final Sprite UPPER_SPRITE = HexLocationSprite.make("caveupper", 0x183, MyColors.BLACK, MyColors.WHITE, MyColors.RED);


    public UnderworldEntrance(Point position) {
        super(position, "Cave Entrance");
    }

    @Override
    public List<DailyAction> getDailyActions(Model model) {
        if (model.isInCaveSystem()) {
            return List.of(new ExitCavesAction(model));
        }
        return List.of(new EnterCavesAction(model));
    }

    @Override
    public void drawLowerHalf(ScreenHandler screenHandler, int x, int y) {
        screenHandler.register(LOWER_SPRITE.getName(), new Point(x, y), LOWER_SPRITE);
    }

    @Override
    public void drawUpperHalf(ScreenHandler screenHandler, int x, int y) {
        screenHandler.register(UPPER_SPRITE.getName(), new Point(x, y), UPPER_SPRITE);
    }

    @Override
    public HelpDialog getHelpDialog(Model model) {
        return new SpecificTerrainHelpDialog(model.getView(), new CaveHex(0, CaveHex.GROUND_COLOR, 0), false);
    }
}
