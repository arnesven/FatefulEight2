package view.subviews;

import model.Model;
import model.SteppingMatrix;
import util.Arithmetics;
import util.MyPair;
import util.MyRandom;
import view.MyColors;
import view.sprites.FilledBlockSprite;
import view.sprites.Sprite;
import view.sprites.Sprite16x16;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public abstract class AimingSubView extends SubView {

    public static final int TARGET_DIAMETER = 30;
    protected static final Point OFFSETS = new Point(TARGET_DIAMETER/2, TARGET_DIAMETER/2 + 2);
    private static final Sprite CURSOR = new Sprite16x16("archerycursor", "arrows.png", 0x12);
    protected static final Sprite greenBlock = new FilledBlockSprite(MyColors.GREEN);
    protected static final Sprite lightBlueBlock = new FilledBlockSprite(MyColors.CYAN);

    private static final java.util.List<MyColors> FLETCH_COLORS = List.of(MyColors.RED, MyColors.GREEN, MyColors.YELLOW,
            MyColors.PURPLE, MyColors.PINK, MyColors.PEACH, MyColors.LIGHT_GRAY, MyColors.CYAN, MyColors.LIGHT_BLUE);

    private SteppingMatrix<Integer> matrix;
    private boolean cursorEnabled = true;
    private List<MyPair<Point, Sprite>> arrows = new ArrayList<>();

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
        drawArrows(model);
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

    public void addArrow(Point p, Sprite sprite) {
        this.arrows.add(new MyPair<>(new Point(X_OFFSET + p.x + OFFSETS.x,
                Y_OFFSET + p.y + OFFSETS.y + 1),
                sprite));
    }

    public void addArrow(Point p) {
        addArrow(p, makeArrowSprite());
    }

    public static Sprite makeArrowSprite() {
        return new Sprite16x16("archeryarrow", "arrows.png", MyRandom.randInt(8),
                MyColors.BLACK, MyColors.BLACK, MyRandom.sample(FLETCH_COLORS), MyColors.BEIGE);
    }

    private void drawArrows(Model model) {
        for (MyPair<Point, Sprite> p : arrows) {
            model.getScreenHandler().register(p.second.getName(), p.first, p.second, 1, -4, -4);
        }
    }

    public void clearArrows() {
        arrows.clear();
    }
}
