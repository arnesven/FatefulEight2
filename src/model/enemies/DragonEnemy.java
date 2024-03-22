package model.enemies;

import model.Model;
import model.classes.Skill;
import model.combat.loot.CombatLoot;
import model.combat.loot.StandardCombatLoot;
import model.enemies.behaviors.EnemyAttackBehavior;
import model.enemies.behaviors.MultiMagicAttackBehavior;
import model.items.Equipment;
import model.items.weapons.Weapon;
import util.MyRandom;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.util.ArrayList;
import java.util.List;

public abstract class DragonEnemy extends BeastEnemy {
    private final int speed;
    private final int damage;
    private final DragonSprite sprite;
    private final DragonInventorySprite inventorySprite;
    private final MyColors[] colorSet;

    public DragonEnemy(char a, String name, int speed, int damage, EnemyAttackBehavior attackBehavior,
                       MyColors[] colorSet) {
        super(a, name, RAMPAGING, attackBehavior);
        this.speed = speed;
        this.damage = damage;
        this.sprite = new DragonSprite(colorSet);
        this.inventorySprite = new DragonInventorySprite(colorSet);
        this.colorSet = colorSet;
    }

    @Override
    public int getMaxHP() {
        return getDragonMaxHp();
    }

    protected abstract int getDragonMaxHp();

    @Override
    public int getSpeed() {
        return speed;
    }

    @Override
    protected Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getDamage() {
        return damage;
    }

    @Override
    public int getWidth() {
        return 2;
    }

    @Override
    protected int getHeight() {
        return 2;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new DragonLoot(model);
    }

    public abstract Skill getMagicSkill();

    public Sprite getInventorySprite() {
        return inventorySprite;
    }

    public MyColors[] getColorSet() {
        return colorSet;
    }

    public abstract Equipment getTamedEquipment();

    private static class DragonLoot extends StandardCombatLoot {
        public DragonLoot(Model model) {
            super(model, MyRandom.randInt(15, 25));
        }
    }

    protected static class DragonSprite extends LoopingSprite {
        public DragonSprite(MyColors[] colors) {
            super("dragon", "enemies.png", 0x32, 64, 64, new ArrayList<>());
            setColor1(colors[0]);
            setColor2(colors[1]);
            setColor3(colors[2]);
            setColor4(colors[3]);
            setFrames(3);
        }
    }

    protected static class DragonInventorySprite extends Sprite {
        public DragonInventorySprite(MyColors[] colors) {
            super("dragon_inventory", "enemies.png",
                    2, 3, 64, 64);
            setColor1(colors[0]);
            setColor2(colors[1]);
            setColor3(colors[2]);
            setColor4(colors[3]);
        }
    }

    public static DragonEnemy generateDragon(char a) {
        int dieRoll = MyRandom.rollD6();
        if (dieRoll < 2) {
            return new IceDragonEnemy(a);
        } else if (dieRoll < 3) {
            return new BlackDragonEnemy(a);
        } else if (dieRoll < 4) {
            return new GreenDragonEnemy(a);
        } else if (dieRoll < 5) {
            return new BoneDragonEnemy(a);
        } else if (dieRoll < 6) {
            return new ElderDragonEnemy(a);
        }
        return new RedDragonEnemy(a);
    }

}
