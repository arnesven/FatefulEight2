package model.states.battle;

import view.MyColors;

public abstract class OrcishBattleUnit extends BattleUnit {

    public OrcishBattleUnit(String name, int count, int combatSkillBonus, int defense,
                            int movementPoints, int maxUnitSize) {
        super(name, count, combatSkillBonus, defense, movementPoints, "Orc", maxUnitSize, 0, 0);
    }

    @Override
    public MyColors getColor() {
        return MyColors.RED;
    }
}
