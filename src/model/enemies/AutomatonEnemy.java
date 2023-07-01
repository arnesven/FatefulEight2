package model.enemies;

import model.Model;
import model.Party;
import model.combat.CombatLoot;
import util.MyRandom;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;


public class AutomatonEnemy extends Enemy {
    private Sprite sprite;

    public AutomatonEnemy(char a, MyColors color) {
        super(a, "Automaton");
        sprite = new AutomatonSprite(color);
    }

    public AutomatonEnemy(char a) {
        this(a, MyColors.GOLD);
    }

    @Override
    public int getMaxHP() {
        return 14;
    }

    @Override
    public int getSpeed() {
        return 6;
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getDamage() {
        return 5;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new AutomatonLoot();
    }

    private static class AutomatonSprite extends LoopingSprite {
        public AutomatonSprite(MyColors color) {
            super("automaton", "enemies.png", 0x98, 32);
            setColor1(MyColors.BLACK);
            setColor2(color);
            setColor3(MyColors.YELLOW);
            setFrames(4);
        }
    }

    private static class AutomatonLoot extends CombatLoot {

        @Override
        public int getMaterials() {
            return MyRandom.rollD10() + 2;
        }

        @Override
        public String getText() {
            return "";
        }

        @Override
        protected void specificGiveYourself(Party party) {    }
    }
}
