package model.states.battle;

public class SilentMoveOrAttackBattleAction extends MoveOrAttackBattleAction {
    public SilentMoveOrAttackBattleAction(BattleUnit activeUnit) {
        super(activeUnit);
        setNoPrompt(true);
    }
}
