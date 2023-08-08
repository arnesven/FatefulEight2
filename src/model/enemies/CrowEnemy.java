package model.enemies;

import model.Model;
import model.combat.CombatLoot;
import model.combat.SingleItemCombatLoot;
import model.combat.StandardCombatLoot;
import model.items.potions.HealthPotion;
import model.states.events.PeskyCrowEvent;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class CrowEnemy extends BeastEnemy {
    public static final Sprite SPRITE = new CrowSprite();

    public CrowEnemy(char a) {
        super(a, "Pesky Crow", NORMAL);
    }

    @Override
    public int getMaxHP() {
        return 1;
    }

    @Override
    public int getSpeed() {
        return 3;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getDamage() {
        return 0;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new SingleItemCombatLoot(new HealthPotion());
    }

    private static class CrowSprite extends LoopingSprite {
        public CrowSprite() {
            super("peskycrow", "enemies.png", 0x44, 32, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.WHITE);
            setColor3(MyColors.DARK_GRAY);
            setColor4(MyColors.GRAY);
            setFrames(3);
        }
    }
}
