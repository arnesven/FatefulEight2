package model.enemies;

import model.Model;
import model.combat.CombatLoot;
import model.combat.PersonCombatLoot;
import model.combat.StandardCombatLoot;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class LizardmanEnemy extends Enemy {
    private static final Sprite SPRITE = new LizardmanSprite();

    public LizardmanEnemy(char enemyGroup) {
        super(enemyGroup, "Lizardman");
    }

    @Override
    public int getMaxHP() {
        return 5;
    }

    @Override
    public int getSpeed() {
        return 6;
    }

    @Override
    public String getDeathSound() {
        return "orc_death";
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

    private static class LizardmanSprite extends LoopingSprite {
        public LizardmanSprite() {
            super("lizardman", "enemies.png", 0x62, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.ORANGE);
            setColor3(MyColors.DARK_RED);
            setColor4(MyColors.LIGHT_GRAY);
            setFrames(1);
        }
    }
}
