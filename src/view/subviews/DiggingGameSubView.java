package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.states.digging.DiggingSpace;
import view.BorderFrame;
import view.MyColors;
import view.sprites.*;

import java.awt.*;
import java.awt.event.KeyEvent;

public class DiggingGameSubView extends TopMenuSubView {

    private static final Sprite CURSOR = new Sprite16x16("shovelcursor", "lotto.png", 0x30,
            MyColors.BLACK, MyColors.GRAY, MyColors.BROWN, MyColors.BEIGE);
    private static final Sprite TALL_GRASS = new Sprite16x16("tallGrass", "lotto.png", 0x50,
            MyColors.YELLOW, MyColors.GREEN, MyColors.BROWN, MyColors.BEIGE);
    private final SteppingMatrix<DiggingSpace> matrix;
    private RunOnceAnimationSprite digAnimation;
    private int bouldersLeft;

    public DiggingGameSubView(SteppingMatrix<DiggingSpace> matrix, int totalBoulders) {
        super(2, new int[]{X_OFFSET+3, X_OFFSET + 20});
        this.matrix = matrix;
        bouldersLeft = totalBoulders;
    }

    @Override
    protected String getUnderText(Model model) {
        return "Enter to dig in space, space to mark it. Boulders left: " + bouldersLeft;
    }

    @Override
    protected String getTitleText(Model model) {
        return "DIGGING GAME";
    }

    @Override
    protected void drawCursor(Model model) {
        Point selectedPoint = matrix.getSelectedPoint();
        Point p = convertToScreen(selectedPoint.x, selectedPoint.y);
        p.x = p.x+1;
        p.y = p.y-1;
        if (digAnimation != null) {
            if (digAnimation.isDone()) {
                AnimationManager.unregister(digAnimation);
                digAnimation = null;
            } else {
                model.getScreenHandler().register(digAnimation.getName(), p, digAnimation, 2, -4, +4);
                return;
            }
        }
        model.getScreenHandler().register(CURSOR.getName(), p, CURSOR);
    }

    @Override
    protected void drawInnerArea(Model model) {
        for (int y = Y_OFFSET+1; y < Y_MAX-1; y+=2) {
            for (int x = X_OFFSET; x < X_MAX; x+=2) {
                model.getScreenHandler().put(x, y, TALL_GRASS);
            }
        }

        for (int y = 0; y < matrix.getRows(); ++y) {
            for (int x = 0; x < matrix.getColumns(); ++x) {
                Point p = convertToScreen(x, y);
                matrix.getElementAt(x, y).drawYourself(model.getScreenHandler(), p.x, p.y);
            }
        }
    }

    private Point convertToScreen(int x, int y) {
        int xOffset = (16 - matrix.getColumns()) / 2;
        int yOffset = (18 - matrix.getRows()) / 2;
        return new Point(X_OFFSET + x * 2 + xOffset*2, Y_OFFSET + y * 2 + 1 + yOffset*2);
    }

    @Override
    protected MyColors getTitleColor(Model model, int i) {
        return MyColors.WHITE;
    }

    @Override
    protected String getTitle(int i) {
        if (i == 0) {
            return "Ask for hint";
        }
        return "Give up";
    }

    @Override
    protected boolean innerHandleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            DiggingSpace space = matrix.getSelectedElement();
            if (!space.isDug()) {
                markSpace(space);
            }
        }
        if (digAnimation == null || digAnimation.isDone()) {
            return matrix.handleKeyEvent(keyEvent);
        }
        return false;
    }

    public void markSpace(DiggingSpace space) {
        space.toggleMarked();
        bouldersLeft = bouldersLeft + (space.isMarked() ? -1 : 1);
    }

    @Override
    protected int getDefaultIndex() {
        return 0;
    }

    @Override
    protected boolean cursorOnBorderToTop() {
        return matrix.getMinimumRow() == matrix.getSelectedPoint().y;
    }

    public void startDigAnimation(boolean fast) {
        this.digAnimation = new RunOnceAnimationSprite("shoveldigging", "lotto.png",
                5, 3, 16, 16, 4, MyColors.BLACK);
        this.digAnimation.setAnimationDelay(fast ? 3 : 8);
        this.digAnimation.setColor2(MyColors.GRAY);
        this.digAnimation.setColor3(MyColors.BROWN);
        this.digAnimation.setColor4(MyColors.TAN);
    }

    public boolean isDiggingDone() {
        return this.digAnimation == null || this.digAnimation.isDone();
    }
}
