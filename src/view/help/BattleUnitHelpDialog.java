package view.help;

import model.Model;
import model.states.battle.BattleUnit;
import model.states.battle.PikemenUnit;
import util.MyLists;
import util.MyStrings;
import view.GameView;
import view.MyColors;
import view.party.DrawableObject;

import java.awt.*;
import java.util.List;

public class BattleUnitHelpDialog extends SubChapterHelpDialog {

    private final BattleUnit unit;

    public BattleUnitHelpDialog(GameView view, BattleUnit unit, String text) {
        super(view, unit.getName(), "\n\n\n\n\n" + unit.getStatsString() + "\n\n" + text);
        this.unit = unit;
        setLevel(2);
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> result = super.buildDecorations(model, xStart, yStart);

        result.add(new DrawableObject(xStart, yStart) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                unit.drawYourself(model.getScreenHandler(), new Point(x+15, y+3), false, 2);
            }
        });

        return result;
    }
}
