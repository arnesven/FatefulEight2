package model.enemies;

import model.Model;
import model.characters.GameCharacter;
import model.combat.loot.CombatLoot;
import model.combat.loot.StandardCombatLoot;
import model.enemies.behaviors.EnemyAttackBehavior;
import model.states.CombatEvent;
import model.states.events.GelatinousBlobEvent;
import util.MyRandom;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.*;

import java.awt.*;

public abstract class GelatinousBlobEnemy extends Enemy {
    private final MyColors primaryColor;
    private final MyColors shadeColor;
    private Sprite blobSprite;
    private boolean hidden = false;
    private MitosisEffect splitAni = null;

    public GelatinousBlobEnemy(char a, MyColors primaryColor, MyColors shadeColor) {
        super(a, "Gelatinous Blob", new BlobAttackBehavior(6));
        this.primaryColor = primaryColor;
        this.shadeColor = shadeColor;
        this.blobSprite = makeBlobSprite(0xF8, primaryColor, shadeColor);
    }

    public abstract GelatinousBlobEnemy copy();

    private LoopingSprite makeBlobSprite(int i, MyColors color3, MyColors color4) {
        LoopingSprite sprite = new LoopingSprite("gelblob", "enemies.png", i, 32);
        sprite.setColor1(MyColors.BLACK);
        sprite.setColor2(MyColors.BEIGE);
        sprite.setColor3(color3);
        sprite.setColor4(color4);
        sprite.setFrames(4);
        return sprite;
    }

    @Override
    public int getMaxHP() {
        return 8;
    }

    @Override
    public int getSpeed() {
        return 1;
    }

    @Override
    public String getDeathSound() {
        return "daemon_death";
    }

    @Override
    protected Sprite getSprite() {
        return blobSprite;
    }

    @Override
    public int getDamage() {
        return 3;
    }

    @Override
    public CombatLoot getLoot(Model model) {
        return new StandardCombatLoot(model);
    }

    @Override
    public void drawYourself(ScreenHandler screenHandler, int xpos, int ypos, Sprite initiativeSymbol) {
        if (!hidden) {
            super.drawYourself(screenHandler, xpos, ypos, initiativeSymbol);
        } else if (splitAni != null) {
            splitAni.drawYourself(screenHandler, xpos, ypos);
        }
    }

    private void doMitosis(Model model, CombatEvent combatEvent) {
        this.hidden = true;
        GelatinousBlobEnemy splitGuy = getMitosisCopy();
        splitGuy.hidden = true;
        this.splitAni = new MitosisEffect(primaryColor, shadeColor);
        do {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!splitAni.isDone());
        this.splitAni = null;
        combatEvent.println(getName() + " split in two!");
        hidden = false;
        splitGuy.hidden = false;
        splitGuy.addToHP(-(getMaxHP() - (getHP() + 1) / 2));
        combatEvent.addEnemy(splitGuy);
        combatEvent.doDamageToEnemy(this, 1, null);
        combatEvent.addFloatyDamage(this, 1, DamageValueEffect.STANDARD_DAMAGE);
    }

    protected GelatinousBlobEnemy getMitosisCopy() {
        return this.copy();
    }

    protected static class BlobAttackBehavior extends EnemyAttackBehavior {

        private final int mitosisTarget;

        public BlobAttackBehavior(int mitosisTarget) {
            this.mitosisTarget = mitosisTarget;
        }

        @Override
        public boolean canAttackBackRow() {
            return false;
        }

        @Override
        public String getUnderText() {
            return "Special";
        }

        @Override
        public void performAttack(Model model, Enemy enemy, GameCharacter target, CombatEvent combatEvent) {
            int dieRoll = MyRandom.rollD6();
            GelatinousBlobEnemy blob = ((GelatinousBlobEnemy)enemy);
            if (dieRoll < mitosisTarget) {
                super.performAttack(model, enemy, target, combatEvent);
            } else {
                blob.doMitosis(model, combatEvent);
            }
        }
    }

    private static class MitosisEffectSprite extends RunOnceAnimationSprite {
        public MitosisEffectSprite(MyColors primaryColor, MyColors shadeColor) {
            super("mitosiseffect", "enemies.png", 4, 17, 32, 32, 4, MyColors.BLACK);
            setColor1(MyColors.BLACK);
            setColor2(MyColors.BEIGE);
            setColor3(primaryColor);
            setColor4(shadeColor);
            setAnimationDelay(6);
        }
    }

    private class MitosisEffect {
        private final MyColors primary;
        private final MyColors shadeColor;
        private Sprite current;
        private int count = 0;
        private int stage = 0;

        public MitosisEffect(MyColors primaryColor, MyColors shadeColor) {
            this.primary = primaryColor;
            this.shadeColor = shadeColor;
            LoopingSprite shake = makeShakeSprite(primaryColor, shadeColor);
            this.current = shake;
        }

        public void drawYourself(ScreenHandler screenHandler, int xpos, int ypos) {
            screenHandler.register(current.getName(), new Point(xpos, ypos), current);
            if (count == 50 && stage == 0) {
                current = new Sprite32x32("stillblob", "enemies.png", 0xF8,
                        MyColors.BLACK, MyColors.BEIGE, primaryColor, shadeColor);
                stage++;
            } else if (stage == 2) {
                current = new MitosisEffectSprite(primaryColor, shadeColor);
                stage++;
            } else if (stage == 1) {
                if (count > 20) {
                    count--;
                } else if (count == 20) {
                    stage++;
                }
            } else if (stage == 0){
                count++;
            }
        }

        public boolean isDone() {
            return stage == 3 && ((RunOnceAnimationSprite)current).isDone();
        }
    }

    protected LoopingSprite makeShakeSprite(MyColors primaryColor, MyColors shadeColor) {
        LoopingSprite shake = makeBlobSprite(0xF8, primaryColor, shadeColor);
        shake.setFrames(2);
        shake.setDelay(3);
        return shake;
    }
}
