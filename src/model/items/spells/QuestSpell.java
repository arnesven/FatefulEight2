package model.items.spells;

import view.MyColors;

public abstract class QuestSpell extends Spell {
    public QuestSpell(String name, int cost, MyColors color, int difficulty, int hpCost) {
        super(name, cost, color, difficulty, hpCost);
    }

    @Override
    protected int getExperience() {
        return 10;
    }

}
