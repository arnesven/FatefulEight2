package model.enemies;

import model.combat.*;
import model.characters.GameCharacter;
import model.Model;
import model.items.spells.TransfigurationSpell;
import model.states.CombatEvent;
import sprites.CombatCursorSprite;
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
    private String name;

    public Enemy(char enemyGroup, String name) {
        this.enemyGroup = enemyGroup;
        this.name = name;
        this.setCurrentHp(getMaxHP());
    }

    public void drawYourself(ScreenHandler screenHandler, int xpos, int ypos, Sprite initiativeSymbol) {
        Sprite spr = getSprite();
        if (hasCondition(TransfiguredCondition.class)) {
            ((TransfiguredCondition)getCondition(TransfiguredCondition.class)).drawYourself(screenHandler, xpos, ypos);
        } else {
            screenHandler.register(spr.getName(), new Point(xpos, ypos), spr);
        }
        int offsetX = getWidth()*4-1;
        int offsetY = getHeight()*4-1;
        screenHandler.register(spr.getName() + enemyGroup, new Point(xpos+offsetX, ypos+offsetY),
                CharSprite.make((char)(enemyGroup - 0x40 + 0x06), MyColors.BLACK, MyColors.WHITE, MyColors.CYAN));
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
            List<GameCharacter> candidates = new ArrayList<>();
            if (isRanged()) {
                candidates.addAll(model.getParty().getBackRow());
            }
            candidates.addAll(model.getParty().getFrontRow());
            candidates.addAll(combatEvent.getAllies());
            if (candidates.isEmpty()) {
                candidates.addAll(model.getParty().getBackRow());
            }
            candidates.removeIf((GameCharacter gc) -> gc.isDead());
            Collections.shuffle(candidates);
            if (!candidates.isEmpty()) {
                GameCharacter randomCharFromFrontRow = candidates.get(0);
                attack(model, randomCharFromFrontRow, combatEvent);
            }
        }
        decreaseTimedConditions(model, combatEvent);
    }

    public boolean isRanged() {
        return false;
    }


    public char getEnemyGroup() {
        return enemyGroup;
    }

    public void attack(Model model, GameCharacter target, CombatEvent combatEvent) {
        target.getAttackedBy(this, model, combatEvent);
        model.getTutorial().combatDamage(model);
        if (target.isDead()) {
            combatEvent.println("!" + target.getName() + " has been slain in combat!");
            if (model.getParty().getPartyMembers().contains(target)) {
                if (target.isLeader() && model.getParty().appointNewLeader()) {
                    combatEvent.println(model.getParty().getLeader().getFullName() + " is now the new leader of the party.");
                }
            }
        }
    }

    public abstract int getDamage();

    @Override
    public Sprite getCombatCursor(Model model) {
        return CombatCursorSprite.DEFAULT_CURSOR;
    }

    public int getThreat() {
        return 5 + getMaxHP() + getDamage() + getSpeed() / 3 + getDamageReduction() * 3;
    }

    public abstract CombatLoot getLoot(Model model);

    public int calculateBaseDamage() {
        int damage = getDamage();
        if (hasCondition(WeakenCondition.class)) {
            damage = Math.max(1, damage - 2);
        }
        while (damage > 0 && MyRandom.randInt(2) == 0) {
            damage--;
        }
        return damage;
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
    public void takeCombatDamage(CombatEvent combatEvent, int damage) {
        if (damage > 0 && getDamageReduction() > 0) {
            int hpBefore = getHP();
            super.takeCombatDamage(combatEvent, Math.max(0, damage - getDamageReduction()));
            combatEvent.println("!Damage was reduced by " + (damage - (hpBefore - getHP())) + "!");
        } else {
            super.takeCombatDamage(combatEvent, damage);
        }
    }

    public int getDamageReduction() {
        return 0;
    }
}
