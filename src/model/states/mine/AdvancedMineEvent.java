package model.states.mine;

import model.Model;
import model.states.DailyEventState;
import model.states.SpellCastException;
import view.subviews.*;

import java.awt.*;

public class AdvancedMineEvent extends DailyEventState {
    private boolean playerHasQuit = false;
    private int currentLevel = 1;
    private int stepsLeft = 99;
    private LogicalMine mine;
    private MineSubView mineSubView;

    public AdvancedMineEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        this.mine = new LogicalMine();
        this.mineSubView = new MineSubView(this, mine);
        CollapsingTransition.transition(model, mineSubView);

        do {
            try {
                waitUntil(mineSubView, FreeMoveAvatarSubView::hasMovesToHandle, true);
                if (!mineSubView.handleMove(model)) {
                    askToExit(model);
                }
            } catch (SpellCastException sce) {

            }
        } while (!playerHasQuit);
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getMovesLeft() {
        return stepsLeft;
    }

    public void askToExit(Model model) {
        print("Do you want to exit the mine? (Y/N) ");
        if (yesNoInput()) {
            playerHasQuit = true;
        }
    }

    public void decrementMoves() {
        stepsLeft--;
    }

    public Point moveToRoom(Model model, AdvancedMineEvent state, MineDirection direction) {
        if (direction == MineDirection.up || direction == MineDirection.down) {
            CollapsingTransition.transition(model, mineSubView, () -> mine.moveToRoom(direction));
        } else {
            SwipingTransition.transition(model, mineSubView, direction, () -> mine.moveToRoom(direction));
        }
        return mine.getStartingPoint();
    }

    public MineRoomLocation getCurrentLocation() {
        return mine.getCurrentLocation();
    }
}
