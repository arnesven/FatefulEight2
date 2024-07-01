package model.states.battle;

public class SilentShootBattleAction extends ShootBattleAction {
    public SilentShootBattleAction(BattleUnit currentUnit, BattleState battleState) {
        super(currentUnit, battleState);
        setNoPrompt(true);
    }
}
