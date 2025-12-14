package model.states.battle;

import model.Model;
import view.subviews.BattleSubView;

import java.util.ArrayList;
import java.util.List;

public class DumbBattleAI extends BattleAI {

    @Override
    protected void startTurnHook(Model model, BattleSubView subView, BattleState battleState, ArrayList<BattleUnit> currentUnits) {

    }

    @Override
    protected void specificActivateUnit(Model model, BattleSubView subView,
                                        BattleState battleState, ArrayList<BattleUnit> currentUnits) {
        if (currentUnits.isEmpty()) {
            throw new IllegalStateException("No more units? Should not happen!");
        }
        model.getLog().waitForAnimationToFinish();
        BattleUnit activeUnit = currentUnits.get(0);
        if (activeUnit.getCount() == 0 ||
                battleState.getPositionForUnit(activeUnit).y == BattleState.BATTLE_GRID_HEIGHT-1) {
            currentUnits.remove(activeUnit);
            return;
        }
        MoveOrAttackBattleAction moveOrAttack = null;
        for (BattleDirection dir : List.of(BattleDirection.south, BattleDirection.west, BattleDirection.east, BattleDirection.north)) {
            if (battleState.canMoveInDirection(activeUnit, dir, true, false)) {
                moveOrAttack = new SilentMoveOrAttackBattleAction(activeUnit);
                moveOrAttack.setParameters(model, battleState, dir);
                if (moveOrAttack.isValid()) {
                    break;
                }
            }
        }

        if (moveOrAttack != null) {
            moveOrAttack.execute(model, battleState, activeUnit);
        } else {
            battleState.println(activeUnit.getQualifiedName() + " does nothing.");
        }
        if (activeUnit.getMP() <= 1) {
            currentUnits.remove(activeUnit);
        }
    }
}
