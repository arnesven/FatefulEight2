package model.states.battle;

import model.Model;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MoveOrAttackBattleAction extends BattleAction {
    private BattleDirection direction = null;
    private int mpCost = 0;
    private boolean isExecuting = false;

    public MoveOrAttackBattleAction(BattleUnit unit) {
        super("Move/Attack", unit);
    }

    @Override
    public void execute(Model model, BattleState battleState, BattleUnit performer) {
        this.isExecuting = true;
        if (this.direction == performer.getDirection()) {
            battleState.moveOrAttack(model, performer, this, direction);
        } else if (this.direction != null) {
            if (!isNoPrompt()) {
                battleState.print("Turn " + getPerformer().getName() + " (1 MP)? (Y/N) ");
            } else {
                battleState.delay(200);
            }
            if (isNoPrompt() || battleState.yesNoInput()) {
                performer.setMP(performer.getMP() - performer.getTurnCost());
                performer.setDirection(this.direction);
            }
        }
    }

    public boolean isValid() {
        return this.direction != null;
    }

    @Override
    public void drawUnit(Model model, BattleState state, boolean withMp, Point p) {
        if (this.direction == null) {
            super.drawUnit(model, state, withMp, p);
        } else if (this.direction == getPerformer().getDirection()) {
            Point p2 = new Point(p.x + direction.dxdy.x*2, p.y + direction.dxdy.y*2);
            super.drawUnit(model, state, withMp, p2);
        } else {
            BattleDirection originalDirection = getPerformer().getDirection();
            getPerformer().setDirection(this.direction);
            super.drawUnit(model, state, withMp, p);
            getPerformer().setDirection(originalDirection);
        }
    }

    @Override
    protected int getMpCost() {
        return mpCost;
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model, BattleState state) {
        if (isExecuting) {
            return false;
        }
        BattleDirection newDirection = null;
        if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            newDirection = BattleDirection.west;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            newDirection = BattleDirection.east;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
            newDirection = BattleDirection.north;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
            newDirection = BattleDirection.south;
        }
        if (newDirection == null) {
            return false;
        }
        setParameters(model, state, newDirection);
        return true;
    }

    public void setParameters(Model model, BattleState state, BattleDirection newDirection) {
        if (getPerformer().getDirection() == newDirection) { // Move forward
            int mpCost = state.movePointCostForDestination(getPerformer(), newDirection);
            if (state.canMoveInDirection(getPerformer(), newDirection, true) &&
                    getPerformer().getMP() >= mpCost) {
                this.direction = newDirection;
                if (state.getOtherUnitInDirection(getPerformer(), direction) != null) { // is attack
                    this.mpCost = getPerformer().getMP();
                } else {
                    this.mpCost = mpCost;
                }
            } else {
                this.direction = null; // Out of bounds, or moving into friendly unit, or not enough MP for move.
                this.mpCost = 0;
            }
        } else if (!newDirection.isOpposite(getPerformer().getDirection())) { // Turning
            if (getPerformer().getMP() >= getPerformer().getTurnCost()) {
                this.direction = newDirection;
                this.mpCost = getPerformer().getTurnCost();
            }
        } else {
            this.direction = null; // Moving backward (illegal)
            this.mpCost = 0;
        }
    }
}
