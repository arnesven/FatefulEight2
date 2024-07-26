package model.states.battle;

public class ShorterShootBattleAction extends ShootBattleAction {
    public static final int LONG_RANGE = 5;
    public static final int SHORT_RANGE = 3;

    public ShorterShootBattleAction(BattleUnit unit, BattleState battleState) {
        super(unit, battleState);
    }

    @Override
    public int getLongRange() {
        return LONG_RANGE;
    }

    @Override
    public int getShortRange() {
        return SHORT_RANGE;
    }
}
