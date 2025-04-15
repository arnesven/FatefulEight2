package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.states.swords.SamuraiSword;
import view.MyColors;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class PickSamuraiSwordSubView extends TopMenuSubView {
    private final SteppingMatrix<SamuraiSword> matrix;

    public PickSamuraiSwordSubView(List<SamuraiSword> swords) {
        super(0, new int[]{6, 10});
        this.matrix = new SteppingMatrix<>(8, 2);
        matrix.addElements(swords);
    }

    @Override
    protected void drawCursor(Model model) {

    }

    @Override
    protected void drawInnerArea(Model model) {
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

    private Point convertToScreen(int col, int row) {
        return new Point(X_OFFSET + col * 4, Y_OFFSET + row * 20 + 1);
    }

    @Override
    protected MyColors getTitleColor(Model model, int i) {
        return MyColors.WHITE;
    }

    @Override
    protected String getTitle(int i) {
        return "SWORDS";
    }

    @Override
    protected boolean innerHandleKeyEvent(KeyEvent keyEvent, Model model) {
        return false;
    }

    @Override
    protected int getDefaultIndex() {
        return 0;
    }

    @Override
    protected String getUnderText(Model model) {
        return "";
    }

    @Override
    protected String getTitleText(Model model) {
        return "SWORDS";
    }

    @Override
    protected boolean cursorOnBorderToTop() {
        return false;
    }
}
