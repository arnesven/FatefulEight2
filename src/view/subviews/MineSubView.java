package view.subviews;

import control.FatefulEight;
import model.Model;
import model.states.mine.AdvancedMineEvent;
import model.states.mine.LogicalMine;
import model.states.mine.MineObject;
import util.MyPair;
import util.MyTriplet;
import view.BorderFrame;
import view.MyColors;
import view.sprites.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class MineSubView extends FreeMoveAvatarSubView {

    private final LogicalMine logicalMine;
    private final AdvancedMineEvent state;
    private final List<MyPair<Point, RunOnceAnimationSprite>> animations = new ArrayList<>();
    private final List<MyTriplet<Point, Sprite, Integer>> floatingAnimations = new ArrayList<>();


    public MineSubView(AdvancedMineEvent state, LogicalMine logicalMine) {
        super(logicalMine.getStartingPoint());
        this.logicalMine = logicalMine;
        this.state = state;
    }

    @Override
    protected void drawOverlay(Model model) {
        for (MyPair<Point, RunOnceAnimationSprite> pair : animations) {
            model.getScreenHandler().register(pair.second.getName(), pair.first, pair.second);
        }
        for (MyPair<Point, RunOnceAnimationSprite> pair : animations) {
            if (pair.second.isDone()) {
                AnimationManager.unregister(pair.second);
            }
        }
        animations.removeIf(p -> p.second.isDone());
        for (MyTriplet<Point, Sprite, Integer> trip : floatingAnimations) {
            model.getScreenHandler().register(trip.second.getName(), trip.first, trip.second, 2, 0, trip.third/3);
            trip.third -= 1;
        }
        floatingAnimations.removeIf(trip -> trip.third < 0);
    }

    @Override
    protected void drawBackground(Model model) {
        Point topPos = convertToScreen(new Point(0, -1));
        topPos.y += 2;
        if (FatefulEight.inDebugMode()) {
            BorderFrame.drawString(model.getScreenHandler(), state.getCurrentLocation().asString(), topPos.x, topPos.y,
                    MyColors.WHITE, MyColors.BLACK);
        } else {
            BorderFrame.drawString(model.getScreenHandler(), "Level " + state.getCurrentLevel(), topPos.x, topPos.y,
                    MyColors.WHITE, MyColors.BLACK);
        }

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
            if (logicalMine.getMatrix().getElementAt(avatarPos.x, avatarPos.y) != null) {
                setAvatarEnabled(false);
                Point toReturn = logicalMine.getMatrix().getElementAt(avatarPos.x, avatarPos.y).bumpedWall(model, state, avatarPos, dxdy);
                setAvatarEnabled(true);
                return toReturn;
            }
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

    public void addAnimation(Point positionFor, BreakingRockAnimation breakingRockAnimation) {
        this.animations.add(new MyPair<>(convertToScreen(positionFor), breakingRockAnimation));
    }

    public void addFloatingAnimation(Point p, Sprite32x32 floatingSprite) {
        Point pos = convertToScreen(p);
        pos.y -= 2;
        this.floatingAnimations.add(new MyTriplet<>(pos, floatingSprite, 48));
    }
}
