package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.PersonCombatLoot;
import view.sprites.HermitSprite;
import view.sprites.Sprite;

public class HermitEnemy extends HumanoidEnemy {
    private static final Sprite SPRITE = new HermitSprite();

    public HermitEnemy(char a) {
        super(a, "Enraged Hermit");
    }

    @Override
    public int getMaxHP() {
        return 2;
    }

    @Override
    public int getSpeed() {
        return 2;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 1;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new PersonCombatLoot(model);
    }

}
