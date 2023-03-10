package model.enemies;

import model.Model;
import model.combat.CombatLoot;
import model.combat.StandardCombatLoot;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class TrollEnemy extends Enemy {
    private static final Sprite SPRITE = new TrollEnemySprite();

    public TrollEnemy(char a) {
        super(a, "Troll");
    }

    @Override
    public int getMaxHP() {
        return 20;
    }

    @Override
    public int getSpeed() {
        return 1;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 5;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new StandardCombatLoot(model);
    }

    private static class TrollEnemySprite extends LoopingSprite {
        public TrollEnemySprite() {
            super("trollenemy", "enemies.png", 0x40, 32, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.LIGHT_GRAY);
            setColor3(MyColors.BROWN);
            setColor4(MyColors.BEIGE);
            setFrames(4);
        }
    }
}
