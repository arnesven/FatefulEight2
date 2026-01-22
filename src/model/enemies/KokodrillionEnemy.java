package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.MonsterCombatLoot;
import model.enemies.behaviors.MultipleBleedAttackBehavior;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;


public class KokodrillionEnemy extends BeastEnemy {
    private static final Sprite SPRITE = new KokodrillionSprite();

    public KokodrillionEnemy(char a) {
        super(a, "Kokodrillion", BeastEnemy.HOSTILE, new MultipleBleedAttackBehavior());
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 6;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new MonsterCombatLoot(model);
    }

    @Override
    public int getMaxHP() {
        return 16;
    }

    @Override
    public int getSpeed() {
        return 5;
    }

    @Override
    public int getDamageReduction() {
        return 1;
    }

    private static class KokodrillionSprite extends LoopingSprite {

        public KokodrillionSprite() {
            super("kokodrillion", "enemies.png", 0x15C, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.DARK_GREEN);
            setColor3(MyColors.RED);
            setColor4(MyColors.BEIGE);
            setFrames(4);
        }
    }
}
