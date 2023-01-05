package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.TownDailyActionState;
import sprites.CombatCursorSprite;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x16;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

public class TownSubView extends AvatarSubView {
    public static final MyColors GROUND_COLOR = MyColors.GREEN;
    public static final MyColors PATH_COLOR = MyColors.DARK_GRAY;
    public static final MyColors STREET_COLOR = MyColors.GRAY;
    private static final Sprite STREET = new Sprite32x32("streetground", "world_foreground.png", 0x02, GROUND_COLOR, PATH_COLOR, MyColors.TAN);
    private static final Sprite STREET_INNER = new Sprite32x32("streetground", "world_foreground.png", 0x02, STREET_COLOR, PATH_COLOR, MyColors.TAN);
    private static final Sprite WATER = makeWaterSprite();
    private static final Sprite DOCK = new Sprite32x32("towndock", "world_foreground.png", 0x42,
            MyColors.LIGHT_BLUE, MyColors.DARK_BLUE, MyColors.BROWN, MyColors.DARK_GRAY);

    private final TownDailyActionState state;
    private final SteppingMatrix<DailyActionNode> matrix;
    private final boolean isCoastal;
    private boolean avatarEnabled = true;

    public TownSubView(TownDailyActionState state, SteppingMatrix<DailyActionNode> matrix, boolean isCoastal) {
        this.state = state;
        this.matrix = matrix;
        this.isCoastal = isCoastal;
    }

    @Override
    protected void specificDrawArea(Model model) {
        drawStreet(model);
        if (isCoastal) {
            drawDocks(model);
        } else {
            // TODO: draw more grass
        }
        drawObjects(model);
        if (avatarEnabled) {
            drawAvatar(model);
        }
        drawCursor(model);
    }

    private void drawDocks(Model model) {
        for (int col = 0; col < TownDailyActionState.TOWN_MATRIX_COLUMNS; ++col) {
            Point p = convertToScreen(new Point(col, 0));
            model.getScreenHandler().put(p.x, p.y, DOCK);
            p.y -= 2;
            model.getScreenHandler().put(p.x, p.y, WATER);
        }
    }

    private void drawCursor(Model model) {
        if (matrix.getSelectedPoint() != null) {
            Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
            Point p = convertToScreen(matrix.getSelectedPoint());
            Point dx = matrix.getSelectedElement().getCursorShift();
            p.translate(dx.x, dx.y);
            model.getScreenHandler().register("recruitcursor", p, cursor, 2);
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
        Point p = convertToScreen(state.getCurrentPosition());
        model.getScreenHandler().register("townavatar", p, model.getParty().getLeader().getAvatarSprite());
    }

    private void drawStreet(Model model) {
        Random random = new Random(1234);
        for (int row = 1; row < TownDailyActionState.TOWN_MATRIX_ROWS; ++row) {
            for (int col = 0; col < TownDailyActionState.TOWN_MATRIX_COLUMNS; ++col) {
                Point p = convertToScreen(new Point(col, row));
                if (col == TownDailyActionState.TOWN_MATRIX_COLUMNS-1
                        || row == TownDailyActionState.TOWN_MATRIX_ROWS-1) {
                    model.getScreenHandler().put(p.x, p.y,
                            GrassCombatTheme.grassSprites[random.nextInt(GrassCombatTheme.grassSprites.length)]);
                } else if (1 <= col && col <= 5 && 2 <= row && row <= 6) {
                    model.getScreenHandler().put(p.x, p.y, STREET_INNER);
                } else {
                    model.getScreenHandler().put(p.x, p.y, STREET);
                }
            }
        }
    }

    @Override
    protected String getUnderText(Model model) {
        return matrix.getSelectedElement().getName();
    }

    @Override
    protected String getTitleText(Model model) {
        return "TOWN - " + (state.isMorning()?"MORNING":"EVENING");
    }

    public static Point convertToScreen(Point p) {
        return new Point(X_OFFSET + p.x*4, Y_OFFSET + p.y*4 + 2);
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        return matrix.handleKeyEvent(keyEvent);
    }


    private static Sprite makeWaterSprite() {
        Sprite sprite = new Sprite32x16("water", "world.png", 0x40);
        sprite.setColor1(MyColors.LIGHT_BLUE);
        sprite.setColor2(MyColors.BLUE);
        return sprite;
    }

    public void animateMovement(Model model, Point from, Point to) {
        setDrawAvatarEnabled(false);
        addMovementAnimation(model.getParty().getLeader().getAvatarSprite(), convertToScreen(from), convertToScreen(to));
        waitForAnimation();
        removeMovementAnimation();
        setDrawAvatarEnabled(true);
    }
}
