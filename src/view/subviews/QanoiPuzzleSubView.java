package view.subviews;


import model.Model;
import model.SteppingMatrix;
import model.mainstory.jungletribe.PyramidPuzzleSubView;
import model.mainstory.jungletribe.QanoiDisc;
import model.mainstory.jungletribe.QanoiPin;
import view.sprites.CombatCursorSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;


public class QanoiPuzzleSubView extends PyramidPuzzleSubView {

    private final SteppingMatrix<QanoiPin> pins;
    private QanoiDisc discCursor = null;

    public QanoiPuzzleSubView(SteppingMatrix<QanoiPin> pins) {
        this.pins = pins;
    }

    @Override
    protected String getUnderText(Model model) {
        return "";
    }

    @Override
    protected String getTitleText(Model model) {
        return "PUZZLE QANOI";
    }

    @Override
    protected void specificDrawArea(Model model) {
        for (int x = 0; x < pins.getColumns(); ++x) {
            Point p = matrixToScreen(x, 6);
            pins.getElementAt(x, 0).drawYourself(model.getScreenHandler(), p);
        }

        drawCursor(model);
    }

    private Point matrixToScreen(int x, int y) {
        return convertToScreen(x*2+1, y);
    }

    private void drawCursor(Model model) {
        Point cursorPos = pins.getSelectedPoint();
        Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
        Point p = matrixToScreen(cursorPos.x, cursorPos.y);
        p.y += 9;
        if (discCursor == null) {
            model.getScreenHandler().register(cursor.getName(), p, cursor, 2);
        } else {
            p.y += 2;
            discCursor.drawYourself(model.getScreenHandler(), p);
        }
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (pins.handleKeyEvent(keyEvent)) {
            return true;
        }
        return super.handleKeyEvent(keyEvent, model);
    }

    public void setDiscCursor(QanoiDisc currentDisc) {
        this.discCursor = currentDisc;
    }
}
