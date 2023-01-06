package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.dailyaction.TownDailyActionState;
import sprites.CombatCursorSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;

public abstract class DailyActionSubView extends AvatarSubView {

    private final AdvancedDailyActionState state;
    private final SteppingMatrix<DailyActionNode> matrix;
    private boolean avatarEnabled = true;

    public DailyActionSubView(AdvancedDailyActionState state, SteppingMatrix<DailyActionNode> matrix) {
        this.state = state;
        this.matrix = matrix;
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
        drawCursor(model);
    }

    protected abstract void drawBackground(Model model);

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

    @Override
    protected String getUnderText(Model model) {
        return matrix.getSelectedElement().getName();
    }

    @Override
    protected String getTitleText(Model model) {
        return getPlaceType() + " - " + (state.isMorning()?"MORNING":"EVENING");
    }

    protected abstract String getPlaceType();

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        return matrix.handleKeyEvent(keyEvent);
    }


    public void animateMovement(Model model, Point from, Point to) {
        setDrawAvatarEnabled(false);
        addMovementAnimation(model.getParty().getLeader().getAvatarSprite(), convertToScreen(from), convertToScreen(to));
        waitForAnimation();
        removeMovementAnimation();
        setDrawAvatarEnabled(true);
    }
}
