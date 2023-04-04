package model.enemies;

import model.Model;
import model.Party;
import model.combat.CombatLoot;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;


public class AutomatonEnemy extends Enemy {
    private static final Sprite SPRITE = new AutomatonSprite();

    public AutomatonEnemy(char a) {
        super(a, "Automaton");
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
        return SPRITE;
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
        public AutomatonSprite() {
            super("automaton", "enemies.png", 0x98, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.GOLD);
            setColor3(MyColors.YELLOW);
            setFrames(4);
        }
    }

    private static class AutomatonLoot extends CombatLoot {
        @Override
        public String getText() {
            return "15 materials";
        }

        @Override
        public int getMaterials() {
            return 15;
        }

        @Override
        protected void specificGiveYourself(Party party) {    }
    }
}
