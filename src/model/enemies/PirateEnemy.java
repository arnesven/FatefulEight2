package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.PirateLoot;
import model.races.Race;
import model.states.events.PirateShipEvent;
import util.MyRandom;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class PirateEnemy extends HumanoidEnemy {
    private final int colorNum;
    private final Sprite[] SPRITES = new PirateEnemySprite[]{
            new PirateEnemySprite(MyColors.DARK_RED), new PirateEnemySprite(MyColors.DARK_BLUE),
            new PirateEnemySprite(MyColors.DARK_GREEN), new PirateEnemySprite(MyColors.DARK_GRAY)
    };
    private final int damage;
    private final int hp;

    public PirateEnemy(char a) {
        super(a, "Pirate");
        colorNum = MyRandom.randInt(4);
        damage = MyRandom.randInt(3, 5);
        hp = MyRandom.randInt(5, 10);
        this.setCurrentHp(getMaxHP());
    }

    @Override
    protected Sprite getSprite() {
        return SPRITES[colorNum];
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new PirateLoot(model);
    }

    @Override
    public int getMaxHP() {
        return hp;
    }

    @Override
    public int getSpeed() {
        return 3;
    }

    public int getThreat() {
        return 17;
    }

    private static class PirateEnemySprite extends LoopingSprite {

        public PirateEnemySprite(MyColors color) {
            super("pirate", "enemies.png", 0xE0, 32, 32);
            setColor1(MyColors.BLACK);
            setColor2(color);
            setColor3(MyRandom.sample(Race.getAllRaces()).getColor());
            setColor4(MyColors.LIGHT_GRAY);
            setFrames(4);
        }
    }
}
