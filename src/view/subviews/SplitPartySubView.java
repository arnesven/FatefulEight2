package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import view.sprites.CombatCursorSprite;
import view.BorderFrame;
import view.MyColors;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class SplitPartySubView extends SubView {
    private static final int X_FRAME_START = X_OFFSET + 5;
    private static final int Y_FRAME_START = Y_OFFSET + 10;
    private static final int FRAME_WIDTH = 21;
    private static final int FRAME_HEIGHT = 24;
    private final SubView previous;
    private final List<GameCharacter> groupA;
    private final List<GameCharacter> groupB;
    private SteppingMatrix<GameCharacter> matrix = new SteppingMatrix<>(4, 4);

    public SplitPartySubView(SubView oldSubView, List<GameCharacter> groupA, List<GameCharacter> groupB) {
        this.previous = oldSubView;
        this.groupA = groupA;
        this.groupB = groupB;
        int col = 0;
        int row = 0;
        for (GameCharacter gc : groupA) {
            matrix.addElement(col, row, gc);
            col++;
            if (col == 2) {
                col = 0;
                row++;
            }
        }
    }

    @Override
    protected void drawArea(Model model) {
        previous.drawArea(model);
        model.getScreenHandler().clearForeground(X_FRAME_START, X_FRAME_START + FRAME_WIDTH,
                                                 Y_FRAME_START, Y_FRAME_START + FRAME_HEIGHT);
        BorderFrame.drawFrame(model.getScreenHandler(), X_FRAME_START, Y_FRAME_START, FRAME_WIDTH, FRAME_HEIGHT,
                MyColors.BLACK, MyColors.WHITE, MyColors.BLUE, true);
        BorderFrame.drawCentered(model.getScreenHandler(), "PARTY SPLIT", Y_FRAME_START + 1, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawCentered(model.getScreenHandler(), "GROUP A     GROUP B ", Y_FRAME_START + 3, MyColors.WHITE, MyColors.BLUE);
        for (int col = 0; col < matrix.getColumns(); ++col) {
            for (int row = 0; row < matrix.getRows(); ++row) {
                GameCharacter gc = matrix.getElementAt(col, row);
                if (gc != null) {
                    Point p = convertToScreen(col, row);
                    model.getScreenHandler().register(gc.getAvatarSprite().getName(), p, gc.getAvatarSprite());
                }
            }
        }
        BorderFrame.drawCentered(model.getScreenHandler(), "DONE", Y_FRAME_START + FRAME_HEIGHT - 2,
                MyColors.BLACK, MyColors.LIGHT_YELLOW);
        drawCursor(model);
    }

    private Point convertToScreen(int col, int row) {
        int xXtra = col > 1 ? 4 : 0;
        return new Point(X_FRAME_START + 4*col + xXtra, Y_FRAME_START + 4*row + 5);
    }

    @Override
    protected String getUnderText(Model model) {
        return matrix.getSelectedElement().getName();
    }

    @Override
    protected String getTitleText(Model model) {
        return previous.getTitleText(model);
    }

    private void drawCursor(Model model) {
        Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
        Point p = new Point(matrix.getSelectedPoint());
        p = convertToScreen(p.x, p.y);
        p.y -= 4;
        model.getScreenHandler().register("splitscursor", p, cursor, 2);
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            shiftCharacter();
        }
        return matrix.handleKeyEvent(keyEvent);
    }

    private void shiftCharacter() {
        GameCharacter gc = matrix.getSelectedElement();
        Point p = matrix.getSelectedPoint();
        matrix.remove(gc);

        int x = 2;
        if (p.x > 1) {
            groupB.remove(gc);
            groupA.add(gc);
            x = 0;
        } else {
            groupA.remove(gc);
            groupB.add(gc);
        }
        for (int y = 0; y < matrix.getRows(); ++y) {
            for (int i = x; i < x + 2; ++i) {
                if (matrix.getElementAt(i, y) == null) {
                    matrix.addElement(i, y, gc);
                    return;
                }
            }
        }
    }
}
