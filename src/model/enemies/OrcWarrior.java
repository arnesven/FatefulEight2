package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.PersonCombatLoot;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class OrcWarrior extends Enemy {

    private static final Sprite SPRITE = new OrcWarriorSprite();

    public OrcWarrior(char enemyGroup) {
        super(enemyGroup, "Orc Warrior");
    }

    @Override
    public int getMaxHP() {
        return 7;
    }

    @Override
    public int getSpeed() {
        return 3;
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

    private static class OrcWarriorSprite extends LoopingSprite {
        public OrcWarriorSprite() {
            super("orcwarrior", "enemies.png", 0x3C, 32, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.TAN);
            setColor3(MyColors.ORC_GREEN);
            setColor4(MyColors.GRAY);
            setFrames(4);
        }
    }
}
