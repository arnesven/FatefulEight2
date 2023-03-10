package model.enemies;

import model.Model;
import model.Party;
import model.combat.CombatLoot;
import model.combat.StandardCombatLoot;
import util.MyRandom;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

import java.util.ArrayList;
import java.util.List;

public class DragonEnemy extends Enemy {
    private static final Sprite SPRITE = new DragonSprite();

    public DragonEnemy(char a) {
        super(a, "Dragon");
    }

    @Override
    public int getMaxHP() {
        return 20;
    }

    @Override
    public int getSpeed() {
        return 6;
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
        return new DragonLoot(model);
    }

    private static class DragonLoot extends StandardCombatLoot {
        public DragonLoot(Model model) {
            super(model, MyRandom.randInt(15, 25));
        }
    }

    private static class DragonSprite extends LoopingSprite {
        public DragonSprite() {
            super("dragon", "enemies.png", 0x32, 64, 64, new ArrayList<>());
            setColor1(MyColors.DARK_BROWN);
            setColor2(MyColors.RED);
            setColor3(MyColors.DARK_RED);
            setColor4(MyColors.GOLD);
            setFrames(3);
        }
    }
}
