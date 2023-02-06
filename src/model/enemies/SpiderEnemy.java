package model.enemies;

import model.Model;
import model.characters.GameCharacter;
import model.combat.CombatLoot;
import model.combat.ParalysisCondition;
import model.combat.StandardCombatLoot;
import model.states.CombatEvent;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class SpiderEnemy extends Enemy {
    private static final Sprite SPRITE = new SpiderSprite();

    public SpiderEnemy(char a) {
        super(a, "Spider");
    }

    @Override
    public int getMaxHP() {
        return 7;
    }

    @Override
    public int getSpeed() {
        return 5;
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
    public void attack(Model model, GameCharacter target, CombatEvent combatEvent) {
        int hpBefore = target.getHP();
        super.attack(model, target, combatEvent);
        if (hpBefore > target.getHP()) {
            // TODO : Paralyze on 9-10.k
            combatEvent.println(target.getName() + " has been paralyzed!");
            target.addCondition(new ParalysisCondition());
        }
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new StandardCombatLoot(model);
    }

    private static class SpiderSprite extends LoopingSprite {
        public SpiderSprite() {
            super("spiderenemy", "enemies.png", 0x47, 32);
            setColor1(MyColors.DARK_GRAY);
            setColor2(MyColors.DARK_RED);
            setColor3(MyColors.RED);
            setFrames(6);
        }
    }
}
