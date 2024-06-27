package model.states.battle;

import model.Model;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MoveOrAttackBattleAction extends BattleAction {
    private BattleDirection direction = null;
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
                battleState.print("Turn " + getPerformer().getName() + "? (Y/N) ");
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
            if (state.canMoveInDirection(getPerformer(), newDirection, true) &&
                    getPerformer().getMP() >= state.MovePointCostForDestination(getPerformer(), newDirection)) {
                this.direction = newDirection;
            } else {
                this.direction = null; // Out of bounds, or moving into friendly unit, or not enough MP for move.
            }
        } else if (!newDirection.isOpposite(getPerformer().getDirection())) { // Turning
            if (getPerformer().getMP() >= getPerformer().getTurnCost()) {
                this.direction = newDirection;
            }
        } else {
            this.direction = null; // Moving backward (illegal)
        }
    }
}
