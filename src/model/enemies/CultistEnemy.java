package model.enemies;

import model.Model;
import model.combat.CombatLoot;
import model.combat.PersonCombatLoot;
import model.races.Race;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class CultistEnemy extends Enemy {
    private static final Sprite SPRITE = new CultistEnemySprite();

    public CultistEnemy(char a) {
        super(a, "Cultist");
    }

    @Override
    public int getMaxHP() {
        return 4;
    }

    @Override
    public int getSpeed() {
        return 4;
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

    private static class CultistEnemySprite extends LoopingSprite {
        public CultistEnemySprite() {
            super("cultist", "enemies.png", 0x94, 32, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.DARK_BLUE);
            setColor3(Race.HIGH_ELF.getColor());
            setColor4(MyColors.ORANGE);
            setFrames(4);
        }
    }
}
