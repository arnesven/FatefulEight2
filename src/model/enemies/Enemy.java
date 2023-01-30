package model.enemies;

import model.combat.CombatLoot;
import model.combat.Combatant;
import model.characters.GameCharacter;
import model.Model;
import model.states.CombatEvent;
import sprites.CombatCursorSprite;
import util.MyRandom;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.CharSprite;
import view.sprites.Sprite;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.Point;

public abstract class Enemy extends Combatant {
    private final char enemyGroup;
    private final String name;

    public Enemy(char enemyGroup, String name) {
        this.enemyGroup = enemyGroup;
        this.name = name;
        this.setCurrentHp(getMaxHP());
    }

    public void drawYourself(ScreenHandler screenHandler, int xpos, int ypos, Sprite initiativeSymbol) {
        Sprite spr = getSprite();
        screenHandler.register(spr.getName(), new Point(xpos, ypos), spr);
        int offsetX = getWidth()*4-1;
        int offsetY = getHeight()*4-1;
        screenHandler.register(spr.getName() + enemyGroup, new Point(xpos+offsetX, ypos+offsetY),
                CharSprite.make((char)(enemyGroup - 0x40 + 0x06), MyColors.BLACK, MyColors.WHITE, MyColors.CYAN));
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

    protected abstract Sprite getSprite();

    public Sprite getAvatar() {
        return getSprite();
    }

    @Override
    public void takeCombatTurn(Model model, CombatEvent combatEvent) {
        List<GameCharacter> candidates = new ArrayList<>();
        candidates.addAll(model.getParty().getFrontRow());
        candidates.removeIf((GameCharacter gc) -> gc.isDead());
        if (candidates.isEmpty()) {
            candidates.addAll(model.getParty().getBackRow());
        }
        Collections.shuffle(candidates);
        if (!candidates.isEmpty()) {
            GameCharacter randomCharFromFrontRow = candidates.get(0);
            attack(model, randomCharFromFrontRow, combatEvent);
        }
    }

    public char getEnemyGroup() {
        return enemyGroup;
    }

    public void attack(Model model, GameCharacter target, CombatEvent combatEvent) {
        target.getAttackedBy(this, model, combatEvent);
        model.getTutorial().combatDamage(model);
        if (target.isDead()) {
            combatEvent.println(target.getName() + " has been slain in combat!");
            if (target.isLeader() && model.getParty().appointNewLeader()) {
                combatEvent.println(model.getParty().getLeader().getFullName() + " is now the new leader of the party.");
            }
        }
    }

    public abstract int getDamage();

    @Override
    public Sprite getCombatCursor(Model model) {
        return CombatCursorSprite.DEFAULT_CURSOR;
    }

    public int getThreat() {
        return 5 + getMaxHP() + getDamage() + getSpeed() / 3;
    }

    public abstract CombatLoot getLoot(Model model);
}
