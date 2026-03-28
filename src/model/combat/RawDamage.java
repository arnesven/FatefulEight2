package model.combat;

import view.MyColors;
import view.sprites.DamageValueEffect;

public class RawDamage extends Damage {
    public RawDamage(int amount) {
        super(amount);
    }

    @Override
    public MyColors getColor() {
        return DamageValueEffect.RAW_DAMAGE;
    }
}
