package view.help;

import model.Model;
import model.combat.conditions.Condition;
import view.GameView;
import view.party.DrawableObject;
import view.sprites.Sprite;

import java.awt.*;
import java.util.List;

public class ConditionHelpDialog extends SubChapterHelpDialog {
    private final Sprite symbol;

    public ConditionHelpDialog(GameView view, Condition cond, String text) {
        super(view, makeTitle(cond),
                "Abbreviation: " + cond.getShortName() +
                "\nSymbol:\n\n" + text);
        this.symbol = cond.getSymbol();
        setLevel(1);
    }

    private static String makeTitle(Condition cond) {
        return cond.getName();
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> textContent = super.buildDecorations(model, xStart, yStart);
        textContent.add(new DrawableObject(xStart+10, yStart+4) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                model.getScreenHandler().register(symbol.getName(), new Point(x, y), symbol);
            }
        });
        return textContent;
    }
}
