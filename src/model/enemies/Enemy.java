package model.enemies;

import model.combat.*;
import model.characters.GameCharacter;
import model.Model;
import model.combat.conditions.*;
import model.combat.loot.CombatLoot;
import model.enemies.behaviors.EnemyAttackBehavior;
import model.enemies.behaviors.MeleeAttackBehavior;
import model.states.CombatEvent;
import util.MyLists;
import view.sprites.CombatCursorSprite;
import util.MyPair;
import util.MyPixel;
import util.MyRandom;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.CharSprite;
import view.sprites.RunOnceAnimationSprite;
import view.sprites.Sprite;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.Point;

public abstract class Enemy extends Combatant {

    private final char enemyGroup;
    private EnemyAttackBehavior combatBehavior;
    private String name;

    public Enemy(char enemyGroup, String name, EnemyAttackBehavior combatBehavior) {
        this.enemyGroup = enemyGroup;
        this.name = name;
        this.setCurrentHp(getMaxHP());
        this.combatBehavior = combatBehavior;
    }

    public Enemy(char group, String name) {
        this(group, name, new MeleeAttackBehavior());
    }

    public void drawYourself(ScreenHandler screenHandler, int xpos, int ypos, Sprite initiativeSymbol) {
        Sprite spr = getSprite();
        Condition cond = MyLists.find(getConditions(), Condition::hasAlternateAvatar);
        if (cond != null) {
            cond.drawYourself(screenHandler, xpos, ypos);
        } else {
            screenHandler.register(spr.getName(), new Point(xpos, ypos), spr);
        }
        int offsetX = getWidth()*4-1;
        int offsetY = getHeight()*4-1;
        screenHandler.register(spr.getName() + enemyGroup, new Point(xpos+offsetX, ypos+offsetY),
                CharSprite.make((char)(enemyGroup - 0x40 + 0x09), MyColors.BLACK, MyColors.WHITE, MyColors.CYAN));
        drawConditions(screenHandler, xpos, ypos+offsetY);
    }

    public int getWidth() {
        return 1;
    }

    protected int getHeight() {
        return 1;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected abstract Sprite getSprite();

    public Sprite getAvatar() {
        return getSprite();
    }

    public void takeCombatTurn(Model model, CombatEvent combatEvent) {
        if (!getsCombatTurn()) {
            combatEvent.println(getName() + "'s turn is skipped.");
        } else {
            List<GameCharacter> candidates = getCandidateTargets(model, combatEvent);
            for (int i = 0; i < combatBehavior.numberOfAttacks(); ++i) {
                while (!candidates.isEmpty()) {
                    Collections.shuffle(candidates);
                    GameCharacter randomTarget = candidates.get(0);
                    if (candidates.size() == 1 || !combatEvent.checkForSneakAvoidAttack(randomTarget)) {
                        attack(model, randomTarget, combatEvent);
                        combatEvent.removeSneaker(randomTarget);
                        candidates.remove(randomTarget);
                        break;
                    }
                }
            }
        }
        decreaseTimedConditions(model, combatEvent);
    }

    private List<GameCharacter> getCandidateTargets(Model model, CombatEvent combatEvent) {
        List<GameCharacter> candidates = new ArrayList<>();
        if (canTargetBackRow()) {
            candidates.addAll(model.getParty().getBackRow());
        }
        candidates.addAll(model.getParty().getFrontRow());
        candidates.addAll(combatEvent.getAllies());
        if (candidates.isEmpty()) {
            candidates.addAll(model.getParty().getBackRow());
        }
        candidates.removeIf(Combatant::isDead);
        candidates.removeIf((GameCharacter gc) -> gc.hasCondition(InvisibilityCondition.class));
        if (hasCondition(ImpaledCondition.class)) {
            ImpaledCondition impCond = (ImpaledCondition) getCondition(ImpaledCondition.class);
            candidates.removeIf((GameCharacter gc) -> gc != impCond.getImapler());
        }
        return candidates;
    }

    public boolean canTargetBackRow() {
        return combatBehavior.canAttackBackRow();
    }

    public char getEnemyGroup() {
        return enemyGroup;
    }

    public final void attack(Model model, GameCharacter target, CombatEvent combatEvent) {
        boolean isRangedAttack = model.getParty().getBackRow().contains(target);
        if (isRangedAttack) {
            combatEvent.print(getName() + " performs a ranged attack! ");
        }
        combatBehavior.performAttack(model, this, target, combatEvent);
        model.getTutorial().combatDamage(model);
        combatEvent.checkForDead(model, target);
        if (!getAttackBehavior().canAttackBackRow()) {
            combatEvent.checkFlameWallDamage(model, this);
        }
    }

    public abstract int getDamage();

    @Override
    public Sprite getCombatCursor(Model model) {
        return CombatCursorSprite.DEFAULT_CURSOR;
    }

    public int getThreat() {
        int damageMultiplier = 1;
        if (!(getAttackBehavior() instanceof MeleeAttackBehavior)) {
            damageMultiplier = 2;
        }
        return 5 + getMaxHP() + (damageMultiplier * getDamage()) + getSpeed() / 3 + getDamageReduction() * 3;
    }

    public abstract CombatLoot getLoot(Model model);

    public MyPair<Integer, Boolean> calculateBaseDamage(boolean isRanged) {
        int damage = combatBehavior.calculateDamage(this, isRanged);
        if (hasCondition(WeakenCondition.class)) {
            damage = Math.max(0, damage - 2);
        }
        if (combatBehavior.isCriticalHit()) {
            damage = damage + 2;
            return new MyPair<>(damage, true);
        }
        if (damage > 0 && MyRandom.randInt(3) == 0) {
            damage--;
        }
        return new MyPair<>(damage, false);
    }

    public RunOnceAnimationSprite getKillAnimation() {
        try {
            return new KillAnimation(makeWhite(getSprite().getImage()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("Could not make kill animation for sprite " + getName());
    }

    private static BufferedImage makeWhite(BufferedImage img) {
        BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        for (int y = 0; y < img.getHeight(); ++y) {
            for (int x = 0; x < img.getWidth(); ++x) {
                MyPixel pix = new MyPixel(img.getRGB(x, y));
                if (pix.getAlpha() == 0xFF) {
                    result.setRGB(x, y, 0xFFFFFFFF);
                }
            }
        }
        return result;
    }

    private static BufferedImage makeLessWhite(BufferedImage img, int i, int max) {
        BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        for (int y = 0; y < img.getHeight(); ++y) {
            for (int x = 0; x < img.getWidth(); ++x) {
                if (img.getRGB(x, y) == 0xFFFFFFFF) {
                    MyPixel pix = new MyPixel(img.getRGB(x, y));
                    pix.setAlpha(0xFF - (i * 0xFF) / max);
                    result.setRGB(x, y, pix.toInt());
                }
            }
        }
        return result;
    }

    public void doUponDeath(Model model, CombatEvent combatEvent, GameCharacter killer) { }

    public RunOnceAnimationSprite getStrikeEffect() {
        return combatBehavior.getStrikeEffect();
    }

    public EnemyAttackBehavior getAttackBehavior() {
        return combatBehavior;
    }

    public boolean isFearless() {
        return false;
    }

    private static class KillAnimation extends RunOnceAnimationSprite {
        private static final int ANIMATION_STEPS = 10;
        private final BufferedImage[] images = new BufferedImage[ANIMATION_STEPS];

        public KillAnimation(BufferedImage img) {
            super("killAnimation", "combat.png", 0, 0, img.getWidth(), img.getHeight(), ANIMATION_STEPS, MyColors.BLUE);
            this.images[0] = img;
            for (int i = 1; i < images.length; ++i) {
                this.images[i] = makeLessWhite(img, i, ANIMATION_STEPS);
            }
            setAnimationDelay(6);
        }

        @Override
        public BufferedImage getImage() throws IOException {
            if (images == null) {
                return null;
            }
            if (getCurrentFrame() >= ANIMATION_STEPS) {
                return images[ANIMATION_STEPS - 1];
            }
            return images[getCurrentFrame()];
        }
    }

    @Override
    public void takeCombatDamage(CombatEvent combatEvent, int damage, GameCharacter damager) {
        if (damage > 0 && getDamageReduction() > 0 && !hasCondition(ErodeCondition.class)) {
            int hpBefore = getHP();
            super.takeCombatDamage(combatEvent, Math.max(0, damage - getDamageReduction()), damager);
            combatEvent.printAlert("Damage was reduced by " + (damage - (hpBefore - getHP())) + "!");
        } else {
            super.takeCombatDamage(combatEvent, damage, damager);
        }
    }

    public int getDamageReduction() {
        return 0;
    }

    public void setAttackBehavior(EnemyAttackBehavior behavior) {
        this.combatBehavior = behavior;
    }
}
