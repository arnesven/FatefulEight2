package model.combat;

import view.MyColors;

public abstract class Damage {
    private int amount;

    public Damage(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public abstract MyColors getColor();

    public void reduceBy(int damageReduction) {
        this.amount = Math.max(0, amount - damageReduction);
    }
}
