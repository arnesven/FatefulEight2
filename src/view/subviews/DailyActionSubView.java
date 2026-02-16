package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import view.combat.GrassCombatTheme;
import view.sprites.AvatarSprite;
import view.sprites.CombatCursorSprite;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class DailyActionSubView extends AvatarSubView {

    public static final int ORTHOGONAL_MOVEMENT = 1;
    public static final int DIRECT_MOVEMENT = 2;

    private final AdvancedDailyActionState state;
    private final SteppingMatrix<DailyActionNode> matrix;
    private final int movementType;
    private boolean avatarEnabled = true;
    private boolean cursorEnabled = true;

    public DailyActionSubView(AdvancedDailyActionState state, SteppingMatrix<DailyActionNode> matrix,
                              int movementType) {
        this.state = state;
        this.matrix = matrix;
        this.movementType = movementType;
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
        AvatarSprite avatar = model.getParty().getLeader().getAvatarSprite();
        if (movementType == DIRECT_MOVEMENT || from.y == to.y) {
            directMovement(avatar, from, to);
        } else { // Orthogonal
            boolean orthoPossible = tryFindOrthoPath(avatar, from, to, false);

            if (!orthoPossible) {
                System.out.println("Searching down from to");
                for (int yExtra = 1; yExtra < 7; ++yExtra) {
                    if (to.y + yExtra > 8 || state.isPositionFilled(to.x, to.y + yExtra)) {
                        break;
                    }
                    Point preMid = new Point(to.x, to.y + yExtra);
                    if (tryFindOrthoPath(avatar, from, preMid, false)) {
                        directMovement(avatar, preMid, to);
                        orthoPossible = true;
                        break;
                    }
                }
            }
            if (!orthoPossible) {
                for (int yExtra = 1; yExtra < 7; ++yExtra) {
                    System.out.println("Search down from from");
                    if (from.y + yExtra > 8 || state.isPositionFilled(from.x, from.y + yExtra)) {
                        break;
                    }
                    Point preMid = new Point(from.x, from.y + yExtra);
                    if (tryFindOrthoPath(avatar, preMid, to, true)) {
                        directMovement(avatar, from, preMid);
                        tryFindOrthoPath(avatar, preMid, to, false);
                        orthoPossible = true;
                        break;
                    }
                }
            }

            if (!orthoPossible) {// no free column
                System.out.println("No ortho movement possible.");
                directMovement(avatar, from, to);
            }
        }
        setDrawAvatarEnabled(true);
    }

    private boolean tryFindOrthoPath(AvatarSprite avatar, Point from, Point to, boolean dryRun) {
        int maxY = Math.max(from.y, to.y);
        int minY = Math.min(from.y, to.y);
        int currentColumn = from.x;
        int freeColumn = -1;
        for (int step = 0; step < 8; ++step) {
            currentColumn = from.x + step;
            if (currentColumn < 8) {
                if (isFreeColumn(currentColumn, minY, maxY)) {
                    freeColumn = currentColumn;
                    break;
                }
            }

            currentColumn = from.x - step;
            if (currentColumn >= 0) {
                if (isFreeColumn(currentColumn, minY, maxY)) {
                    freeColumn = currentColumn;
                    break;
                }
            }
        }

        if (freeColumn >= 0) {
            if (!dryRun) {
                System.out.println("Ortho movment from " + from);
                Point mid = new Point(freeColumn, from.y);
                System.out.println(" .... to " + mid);
                directMovement(avatar, from, mid);
                Point mid2 = new Point(freeColumn, to.y);
                System.out.println(" .... to " + mid2);
                directMovement(avatar, mid, mid2);
                System.out.println(" .... and finally to " + to);
                directMovement(avatar, mid2, to);
            }
            return true;
        }
        return false;
    }

    private void directMovement(AvatarSprite avatarSprite, Point from, Point to) {
        addMovementAnimation(avatarSprite, convertToScreen(from), convertToScreen(to));
        waitForAnimation();
        removeMovementAnimation();
    }

    private boolean isFreeColumn(int currentColumn, int minY, int maxY) {
        for (int y = maxY; y >= minY; --y) {
            if (state.isPositionFilled(currentColumn, y)) {
                return false;
            }
        }
        return true;
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

    protected void drawSmallRoom(Model model, Sprite lowerWallSprite, Sprite doorSpriteToUse, int doorX) {
        drawRoom(model, 0, 7, 1, 6, lowerWallSprite, doorSpriteToUse, doorX);
    }


    public static void drawRoom(Model model, int colStart, int colEnd, int rowStart, int rowEnd,
                                Sprite lowerWallSprite, Sprite doorSpriteToUse, int doorX) {
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
                    if (col == doorX) {
                        model.getScreenHandler().put(p.x, p.y, doorSpriteToUse);
                    } else {
                        model.getScreenHandler().put(p.x, p.y, lowerWallSprite);
                    }
                } else {
                    model.getScreenHandler().put(p.x, p.y,
                            GrassCombatTheme.grassSprites[random.nextInt(GrassCombatTheme.grassSprites.length)]);
                }
            }
        }
    }
}
