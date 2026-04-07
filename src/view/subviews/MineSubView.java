package view.subviews;

import model.Model;
import model.states.mine.AdvancedMineEvent;
import model.states.mine.LogicalMine;
import model.states.mine.MineObject;
import view.BorderFrame;
import view.MyColors;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MineSubView extends FreeMoveAvatarSubView {

    private final LogicalMine logicalMine;
    private final AdvancedMineEvent state;

    public MineSubView(AdvancedMineEvent state, LogicalMine logicalMine) {
        super(logicalMine.getStartingPoint());
        this.logicalMine = logicalMine;
        this.state = state;
    }

    @Override
    protected void drawOverlay(Model model) {

    }

    @Override
    protected void drawBackground(Model model) {
        Point topPos = convertToScreen(new Point(0, -1));
        topPos.y += 2;
        //BorderFrame.drawString(model.getScreenHandler(), "Level " + state.getCurrentLevel(), topPos.x, topPos.y,
        //        MyColors.WHITE, MyColors.BLACK);
        BorderFrame.drawString(model.getScreenHandler(), state.getCurrentLocation().asString(), topPos.x, topPos.y,
                MyColors.WHITE, MyColors.BLACK);

        BorderFrame.drawString(model.getScreenHandler(), String.format("Moves Left:%3d", state.getMovesLeft()),
                topPos.x+18, topPos.y,
                MyColors.WHITE, MyColors.BLACK);
        for (int row = 0; row < logicalMine.getMatrix().getRows(); ++row) {
            for (int col = 0; col < logicalMine.getMatrix().getColumns(); ++col) {
                MineObject obj = logicalMine.getMatrix().getElementAt(col, row);
                if (obj != null) {
                    Point pos = convertToScreen(new Point(col, row));
                    obj.drawYourself(model.getScreenHandler(),
                            logicalMine, pos);
                }
            }
        }
    }

    @Override
    protected Point moveAvatar(Model model, KeyEvent key, Point avatarPos, Point dxdy) {
        Point newPosition = new Point(avatarPos.x + dxdy.x, avatarPos.y + dxdy.y);
        if (!logicalMine.canMoveInto(model, state, newPosition)) {
            return avatarPos;
        }
        state.decrementMoves();
        setAvatarEnabled(false);
        addMovementAnimation(model.getParty().getLeader().getAvatarSprite(),
                convertToScreen(avatarPos), convertToScreen(newPosition));
        waitForAnimation();
        MineObject objAtDestination = logicalMine.getMatrix().getElementAt(newPosition.x, newPosition.y);
        removeMovementAnimation();
        if (objAtDestination != null) {
            newPosition = objAtDestination.gotMovedInto(model, state, newPosition);
        }
        setAvatarEnabled(true);
        return newPosition;
    }

    @Override
    protected String getUnderText(Model model) {
        return "Move with the arrow keys, SPACE to quit.";
    }

    @Override
    protected String getTitleText(Model model) {
        return "EVENT - MINE";
    }
}
