package model.enemies;

import model.Model;
import model.combat.BossCombatLoot;
import model.combat.CombatLoot;
import model.combat.StandardCombatLoot;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class VampireEnemy extends UndeadEnemy {
    private static final LoopingSprite SPRITE = new VampireEnemySprite();

    public VampireEnemy(char a) {
        super(a, "Vampire");
    }

    @Override
    public int getMaxHP() {
        return 14;
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
        return 7;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new BossCombatLoot(model);
    }

    private static class VampireEnemySprite extends LoopingSprite {
        public VampireEnemySprite() {
            super("vampire", "enemies.png", 0x1C, 32, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.DARK_PURPLE);
            setColor3(MyColors.WHITE);
            setColor4(MyColors.DARK_RED);
            setFrames(4);
        }
    }
}
