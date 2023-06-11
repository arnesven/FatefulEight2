package model.enemies;

import model.Model;
import model.combat.CombatLoot;
import view.sprites.FrogManSprite;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class FrogmanScoutEnemy extends Enemy {
    private static final Sprite SPRITE = new FrogManSprite(0xC4);

    public FrogmanScoutEnemy(char a) {
        super(a, "Frogman Scout");
    }

    @Override
    public int getMaxHP() {
        return 3;
    }

    @Override
    public int getSpeed() {
        return 8;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 2;
    }

    @Override
    protected int getFightingStyle() {
        return Enemy.FIGHTING_STYLE_RANGED;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return null;
    }
}
