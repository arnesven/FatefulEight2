package model.enemies;

import model.Model;
import model.combat.loot.CombatLoot;
import model.combat.loot.PersonCombatLoot;
import model.races.Race;
import util.MyRandom;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

import java.util.List;

public class MuggerEnemy extends HumanoidEnemy {

    private static List<Sprite> SPRITES = List.of(new MuggerSprite(Race.NORTHERN_HUMAN),
            new MuggerSprite(Race.DARK_ELF), new MuggerSprite(Race.HALF_ORC),
            new MuggerSprite(Race.SOUTHERN_HUMAN), new MuggerSprite(Race.WOOD_ELF));
    private final Sprite sprite;

    public MuggerEnemy(char enemyGroup) {
        super(enemyGroup, "Mugger");
        sprite = MyRandom.sample(SPRITES);
    }

    @Override
    public int getSpeed() {
        return 3;
    }

    @Override
    public int getMaxHP() {
        return 4;
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getDamage() {
        return 2;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new PersonCombatLoot(model);
    }

    protected static class MuggerSprite extends LoopingSprite {
        public MuggerSprite(Race race) {
            super("mugger", "enemies.png", 0x2C, 32, 32);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.GRAY);
            setColor3(race.getColor());
            setColor4(MyColors.DARK_GREEN);
            setFrames(4);
        }
    }
}
