package model.states.battle;

import model.Model;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MoveOrAttackBattleAction extends BattleAction {
    private BattleUnit.Direction direction = null;

    public MoveOrAttackBattleAction(BattleUnit unit) {
        super(unit);
    }

    @Override
    public void execute(Model model, BattleState battleState, BattleUnit performer) {
        if (this.direction == performer.getDirection()) {
            battleState.moveOrAttack(model, performer, this, direction);
        } else if (this.direction != null) {
            battleState.print("Turn " + getPerformer().getName() + "? (Y/N)");
            if (battleState.yesNoInput()) {
                performer.setDirection(this.direction);
            }
        }

    }

    @Override
    public void drawUnit(Model model, BattleState state, Point p) {
        if (this.direction == null) {
            super.drawUnit(model, state, p);
        } else if (this.direction == getPerformer().getDirection()) {
            Point p2 = new Point(p.x + direction.dxdy.x*4, p.y + direction.dxdy.y*4);
            super.drawUnit(model, state, p2);
        } else {
            BattleUnit copy = getPerformer().copy();
            copy.setDirection(this.direction);
            copy.drawYourself(model.getScreenHandler(), p, 3);
            drawMarker(model, p);
        }
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model, BattleState state) {
        BattleUnit.Direction newDirection = null;
        if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            newDirection = BattleUnit.Direction.west;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            newDirection = BattleUnit.Direction.east;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
            newDirection = BattleUnit.Direction.north;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
            newDirection = BattleUnit.Direction.south;
        }
        if (newDirection == null) {
            return false;
        }
        if (getPerformer().getDirection() == newDirection) { // Move forward
            if (state.canMoveInDirection(getPerformer(), newDirection)) {
                this.direction = newDirection;
            } else {
                this.direction = null; // Out of bounds, or moving into friendly unit
            }
        } else if (!newDirection.isOpposite(getPerformer().getDirection())) { // Turning
            this.direction = newDirection;
        } else {
            this.direction = null; // Moving backward (illegal)
        }
        return true;
    }
}
