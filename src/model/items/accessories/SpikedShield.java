package model.items.accessories;

import model.enemies.Enemy;
import model.items.Item;
import model.items.Prevalence;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.DamageValueEffect;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class SpikedShield extends ShieldItem {
    private static final Sprite SPRITE = new ItemSprite(4, 3);

    public SpikedShield() {
        super("Spiked Shield", 30, true, 1);
    }

    @Override
    public int getAP() {
        return 2;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getExtraText() {
        return super.getExtraText() + ", Deals 1 damage to attacker on hit";
    }

    @Override
    public Item copy() {
        return new SpikedShield();
    }

    @Override
    public void wielderWasAttackedBy(Enemy enemy, CombatEvent combatEvent) {
        combatEvent.println(this.getName() + " deals 1 damage to " + enemy.getName() + ".");
        combatEvent.addFloatyDamage(enemy, 1, DamageValueEffect.STANDARD_DAMAGE);
        combatEvent.doDamageToEnemy(enemy, 1, null);
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
