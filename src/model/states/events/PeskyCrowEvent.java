package model.states.events;

import model.Model;
import model.combat.CombatLoot;
import model.combat.NoCombatLoot;
import model.enemies.Enemy;
import model.states.DailyEventState;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

import java.util.List;

public class PeskyCrowEvent extends DailyEventState {
    public PeskyCrowEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("\"Caw caw!\"");
        println("You suddenly notice that a crow has been following you for some time. At first you " +
                "try to shoo it away but it keeps coming closer and closer like it's drawn to you. Suddenly it attacks you!");
        runCombat(List.of(new CrowEnemy('A')));
    }

    private class CrowEnemy extends Enemy {
        private final Sprite SPRITE = new CrowSprite();

        public CrowEnemy(char a) {
            super(a, "Pesky Crow");
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
            return new NoCombatLoot();
        }
    }

    private class CrowSprite extends LoopingSprite {
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
