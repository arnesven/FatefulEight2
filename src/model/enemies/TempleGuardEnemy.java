package model.enemies;

import model.Model;
import model.combat.CombatLoot;
import model.combat.PersonCombatLoot;
import model.races.Race;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

import java.util.List;

public class TempleGuardEnemy extends Enemy {
    private static final Sprite SPRITE = new TempleGuardSprite();

    public TempleGuardEnemy(char a) {
        super(a, "Temple Guard");
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
        return 3;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new PersonCombatLoot(model);
    }

    private static class TempleGuardSprite extends LoopingSprite {
        public TempleGuardSprite() {
            super("templeguards", "enemies.png", 0x9C, 32);
            setFrames(4);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.LIGHT_YELLOW);
            setColor3(Race.NORTHERN_HUMAN.getColor());
            setColor4(MyColors.GRAY);
        }
    }
}