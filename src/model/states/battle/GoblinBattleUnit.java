package model.states.battle;

import view.MyColors;

public abstract class GoblinBattleUnit extends BattleUnit {

    private final int routThreshold;

    public GoblinBattleUnit(String name, int count, int combatSkillBonus, int defense, int movementPoints, int maxUnitSize, int routThreshold) {
        super(name, count, combatSkillBonus, defense, movementPoints, "Goblin", maxUnitSize, 0, 0);
        this.routThreshold = routThreshold;
    }

    @Override
    public int getRoutThreshold() {
        return routThreshold;
    }

    @Override
    public MyColors getColor() {
        return MyColors.RED;
    }
}
