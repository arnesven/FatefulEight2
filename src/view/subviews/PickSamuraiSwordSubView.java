package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.states.swords.SamuraiSword;
import view.sprites.CombatCursorSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class PickSamuraiSwordSubView extends SubView {
    private final SteppingMatrix<SamuraiSword> matrix;

    public PickSamuraiSwordSubView(List<SamuraiSword> swords) {
        super(0);
        this.matrix = new SteppingMatrix<>(8, 2);
        matrix.addElements(swords);
    }

    @Override
    protected void drawArea(Model model) {
        drawSwords(model);
        drawCursor(model);
    }

    private void drawSwords(Model model) {
        for (int row = 0; row < matrix.getRows(); ++row) {
            for (int col = 0; col < matrix.getColumns(); ++col) {
                SamuraiSword sw = matrix.getElementAt(col, row);
                if (sw != null) {
                    Point p = convertToScreen(col, row);
                    sw.drawYourself(model, p.x, p.y);
                }
            }
        }
    }

    private void drawCursor(Model model) {
        if (matrix.getSelectedPoint() != null) {
            Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
            Point p = new Point(matrix.getSelectedPoint());
            p = convertToScreen(p.x, p.y);
            p.y += matrix.getSelectedElement().getCursorOffset();
            model.getScreenHandler().register("recruitcursor", p, cursor, 2);
        }
    }

    private Point convertToScreen(int col, int row) {
        return new Point(X_OFFSET + col * 4, Y_OFFSET + row * 20);
    }

    @Override
    protected String getUnderText(Model model) {
        return "";
    }

    @Override
    protected String getTitleText(Model model) {
        return "EVENT";
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        return matrix.handleKeyEvent(keyEvent);
    }
}
