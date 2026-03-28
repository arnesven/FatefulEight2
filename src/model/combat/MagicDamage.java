package model.combat;

import view.MyColors;
import view.sprites.DamageValueEffect;

public class MagicDamage extends Damage {
    public MagicDamage(int amount) {
        super(amount);
    }

    @Override
    public MyColors getColor() {
        return DamageValueEffect.MAGICAL_DAMAGE;
    }
}
