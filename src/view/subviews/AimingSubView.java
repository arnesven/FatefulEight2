package view.subviews;

import model.Model;
import model.SteppingMatrix;
import util.Arithmetics;
import view.MyColors;
import view.sprites.FilledBlockSprite;
import view.sprites.Sprite;
import view.sprites.Sprite16x16;

import java.awt.*;
import java.awt.event.KeyEvent;

public abstract class AimingSubView extends SubView {

    public static final int TARGET_DIAMETER = 30;
    protected static final Point OFFSETS = new Point(TARGET_DIAMETER/2, TARGET_DIAMETER/2 + 2);
    private static final Sprite CURSOR = new Sprite16x16("archerycursor", "arrows.png", 0x12);
    protected static final Sprite greenBlock = new FilledBlockSprite(MyColors.GREEN);
    protected static final Sprite lightBlueBlock = new FilledBlockSprite(MyColors.CYAN);

    private SteppingMatrix<Integer> matrix;
    private boolean cursorEnabled = true;

    public AimingSubView() {
        this.matrix = new SteppingMatrix<>(32, 36);
        matrix.setSoundEnabled(false);
        int counter = 0;
        for (int y = 0; y < matrix.getRows(); ++y) {
            for (int x = 0; x < matrix.getColumns(); ++x) {
                if (x == OFFSETS.x && y == OFFSETS.y) {
                    this.matrix.addElement(x, y, -1);
                } else {
                    this.matrix.addElement(x, y, counter++);
                }
            }
        }
        matrix.setSelectedPoint(-1);
    }

    @Override
    protected void drawArea(Model model) {
        innerDrawArea(model);
        if (cursorEnabled) {
            drawCursor(model);
        }
    }

    protected abstract void innerDrawArea(Model model);

    private void drawCursor(Model model) {
        Point p = new Point(matrix.getSelectedPoint());
        p.x += X_OFFSET;
        p.y += Y_OFFSET;
        model.getScreenHandler().register(CURSOR.getName(), p, CURSOR, 2, -4, -4);
    }

    public void setCursorEnabled(boolean b) {
        this.cursorEnabled = b;
    }

    public Point getAim() {
        Point p = new Point(matrix.getSelectedPoint());
        p.x -= OFFSETS.x;
        p.y -= OFFSETS.y;
        return p;
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (innerHandleKeyEvent(keyEvent, model)) {
            return true;
        }
        return matrix.handleKeyEvent(keyEvent);
    }

    protected abstract boolean innerHandleKeyEvent(KeyEvent keyEvent, Model model);
}
