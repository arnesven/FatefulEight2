package model.enemies;

import model.Model;
import model.combat.CombatLoot;
import model.combat.StandardCombatLoot;
import model.races.Race;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class HermitEnemy extends Enemy {
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
        return new StandardCombatLoot(model);
    }

    private static class HermitSprite extends LoopingSprite {
        public HermitSprite() {
            super("hermit", "enemies.png", 0x70, 32, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.TAN);
            setColor3(Race.HIGH_ELF.getColor());
            setColor4(MyColors.LIGHT_GRAY);
            setFrames(4);
        }
    }
}
