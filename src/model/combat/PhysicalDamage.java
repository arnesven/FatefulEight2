package model.combat;

import view.MyColors;
import view.sprites.DamageValueEffect;

public class PhysicalDamage extends Damage {
    public PhysicalDamage(int amount) {
        super(amount);
    }

    @Override
    public MyColors getColor() {
        return DamageValueEffect.STANDARD_DAMAGE;
    }
}
