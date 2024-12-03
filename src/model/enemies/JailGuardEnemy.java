package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.PersonCombatLoot;
import view.MyColors;
import view.sprites.SoldierSprite;
import view.sprites.Sprite;

public class JailGuardEnemy extends HumanoidEnemy {

    private static final Sprite SPRITE = new SoldierSprite(MyColors.GOLD);

    public JailGuardEnemy(char group) {
        super(group, "Guard");
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 4;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new PersonCombatLoot(model);
    }

    @Override
    public int getMaxHP() {
        return 6;
    }

    @Override
    public int getDamageReduction() {
        return 1;
    }

    @Override
    public int getSpeed() {
        return 4;
    }
}
