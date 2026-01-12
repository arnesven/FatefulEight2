package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.PersonCombatLoot;
import util.MyPair;
import util.MyRandom;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

import java.util.List;

public class LizardmanEnemy extends Enemy {

    private static final List<LizardmanSprite> SPRITES = List.of(
            new LizardmanSprite(MyColors.GRAY_RED, MyColors.DARK_RED),
            new LizardmanSprite(MyColors.BROWN, MyColors.DARK_RED),
            new LizardmanSprite(MyColors.TAN, MyColors.ORANGE),
            new LizardmanSprite(MyColors.TAN, MyColors.DARK_PURPLE),
            new LizardmanSprite(MyColors.DARK_GREEN, MyColors.DARK_PURPLE),
            new LizardmanSprite(MyColors.DARK_RED, MyColors.DARK_BROWN)
    );

    private Sprite sprite = MyRandom.sample(SPRITES);

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
        return sprite;
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
        public LizardmanSprite(MyColors color2, MyColors color3) {
            super("lizardman", "enemies.png", 0x158, 32);
            setColor1(MyColors.BLACK);
            setColor2(color2);
            setColor3(color3);
            setColor4(MyColors.GOLD);
            setFrames(4);
        }
    }
}
