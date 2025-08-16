package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import view.combat.GrassCombatTheme;
import view.sprites.CombatCursorSprite;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

public abstract class DailyActionSubView extends AvatarSubView {

    private final AdvancedDailyActionState state;
    private final SteppingMatrix<DailyActionNode> matrix;
    private boolean avatarEnabled = true;
    private boolean cursorEnabled = true;

    public DailyActionSubView(AdvancedDailyActionState state, SteppingMatrix<DailyActionNode> matrix) {
        this.state = state;
        this.matrix = matrix;
    }

    public SteppingMatrix<DailyActionNode> getMatrix() {
        return matrix;
    }

    public static Point convertToScreen(Point p) {
        return new Point(X_OFFSET + p.x*4, Y_OFFSET + p.y*4 + 2);
    }

    @Override
    protected final void specificDrawArea(Model model) {
        drawBackground(model);
        drawObjects(model);
        if (avatarEnabled) {
            drawAvatar(model);
        }
        if (cursorEnabled) {
            drawCursor(model);
        }
    }

    protected abstract void drawBackground(Model model);

    private void drawCursor(Model model) {
        if (matrix.getSelectedPoint() != null) {
            Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
            Point p = convertToScreen(matrix.getSelectedPoint());
            Point dx = matrix.getSelectedElement().getCursorShift();
            p.translate(dx.x, dx.y);
            model.getScreenHandler().register("recruitcursor", p, cursor, 5);
        }
    }

    private void drawObjects(Model model) {
        for (DailyActionNode dan : matrix.getElementList()) {
            Point p = convertToScreen(matrix.getPositionFor(dan));
            dan.drawYourself(model, p);
        }
    }

    public void setDrawAvatarEnabled(boolean b) {
        avatarEnabled = b;
    }

    private void drawAvatar(Model model) {
        if (model.getParty().getLeader() != null) { // If party has been wiped out and this is just before game over screen
            Point p = convertToScreen(state.getCurrentPosition());
            model.getScreenHandler().register("townavatar", p, model.getParty().getLeader().getAvatarSprite(), 2);
        }
    }

    @Override
    protected String getUnderText(Model model) {
        return matrix.getSelectedElement().getName();
    }

    @Override
    protected String getTitleText(Model model) {
        return getPlaceType() + " - " + (state.isMorning()?"MORNING":(state.isEvening()?"EVENING":"MIDDAY"));
    }

    protected abstract String getPlaceType();

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (cursorEnabled) {
            return matrix.handleKeyEvent(keyEvent);
        }
        return false;
    }


    public void animateMovement(Model model, Point from, Point to) {
        setDrawAvatarEnabled(false);
        addMovementAnimation(model.getParty().getLeader().getAvatarSprite(), convertToScreen(from), convertToScreen(to));
        waitForAnimation();
        removeMovementAnimation();
        setDrawAvatarEnabled(true);
    }

    public void setCursorEnabled(boolean b) {
        cursorEnabled = b;
    }

    protected void drawForeground(Model model, int x, int y, Sprite sprite, int prio) {
        Point p = convertToScreen(new Point(x, y));
        model.getScreenHandler().register(sprite.getName(), p, sprite, prio);
    }

    protected void drawForeground(Model model, int x, int y, Sprite sprite) {
        drawForeground(model, x, y, sprite, 0);
    }

    protected void drawPartyArea(Model model, List<Point> points) {
        int i = 0;
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            if (!gc.isLeader()) {
                LoopingSprite spr = gc.getAvatarSprite();
                spr.synch();
                drawForeground(model, points.get(i).x, points.get(i).y, spr);
                i++;
            }
        }
    }

    protected void drawSmallRoom(Model model, Sprite lowerWallSprite) {
        drawRoom(model, 0, 7, 1, 6, lowerWallSprite);
    }


    public static void drawRoom(Model model, int colStart, int colEnd, int rowStart, int rowEnd, Sprite lowerWallSprite) {
        Random random = new Random(4312);
        for (int row = rowStart; row < rowEnd+2; ++row) {
            for (int col = colStart; col < colEnd; ++col) {
                Point p = convertToScreen(new Point(col, row));
                if ((col == colStart || col == colEnd-1) && row < rowEnd) {
                    model.getScreenHandler().put(p.x, p.y, TavernSubView.SIDE_WALL);
                } else if (rowStart < row && row < rowEnd) {
                    model.getScreenHandler().put(p.x, p.y, TavernSubView.FLOOR);
                } else if (row == rowStart) {
                    model.getScreenHandler().put(p.x, p.y, TavernSubView.WALL);
                } else if (row == rowEnd) {
                    model.getScreenHandler().put(p.x, p.y, lowerWallSprite);
                } else {
                    model.getScreenHandler().put(p.x, p.y,
                            GrassCombatTheme.grassSprites[random.nextInt(GrassCombatTheme.grassSprites.length)]);
                }
            }
        }
    }
}
