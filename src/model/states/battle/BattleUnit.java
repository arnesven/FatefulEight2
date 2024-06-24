package model.states.battle;

import model.Model;
import util.MyRandom;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.io.Serializable;

public abstract class BattleUnit implements Serializable {

    private final String name;
    private int count;
    private final String origin;
    private BattleDirection direction = BattleDirection.east;
    private final int combatSkillBonus;
    private final int defense;

    public BattleUnit(String name, int count, int combatSkillBonus, int defense, String origin) {
        this.name = name;
        this.count = count;
        this.combatSkillBonus = combatSkillBonus;
        this.defense = defense;
        this.origin = origin;
    }

    public abstract BattleUnit copy();

    protected abstract Sprite[] getSprites();

    public void drawYourself(ScreenHandler screenHandler, Point p, int prio) {
        Sprite spr = getSprites()[direction.value];
        screenHandler.register(spr.getName(), p, spr, prio);
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public String getOrigin() {
        return origin;
    }

    public void setDirection(BattleDirection dir) {
        this.direction = dir;
    }

    public BattleDirection getDirection() {
        return direction;
    }

    public void doAttackOn(Model model, BattleState battleState, BattleUnit defender, BattleDirection attackDirection) {
        battleState.println(getQualifiedName() + " attacks " + defender.getQualifiedName() + "!");
        int hits = 0;
        for (int i = 0; i < getCount(); ++i) {
            hits += doOneAttack(battleState, defender) ? 1 : 0;
        }
        int counterHits = 0;
        for (int i = 0; i < defender.getCount(); ++i) {
            counterHits += defender.doOneAttack(battleState, this) ? 1 : 0;
        }
        battleState.println("Attacker does " + hits + " hits, defender does " + counterHits + " hits.");

        boolean attackerWins = false;
        if (defender.getCount() <= hits) {
            defender.wipeOut(battleState);
            attackerWins = true;
        } else {
            defender.setCount(defender.getCount() - hits);
            attackerWins = hits > counterHits;
        }

        if (getCount() <= counterHits) {
            this.wipeOut(battleState);
        } else {
            setCount(getCount() - counterHits);
        }

        if (getCount() > 0) {
            if (attackerWins) {
                if (defender.getCount() > 0) {
                    battleState.retreatUnit(defender, attackDirection);
                }
                battleState.moveUnitInDirection(this, attackDirection);
            } else {
                battleState.println("Attacker has lost the fight and must retreat back to its original space.");
                defender.setDirection(attackDirection.getOpposite());
            }
        }
    }

    public void wipeOut(BattleState battleState) {
        setCount(0);
        battleState.removeUnit(this);
        battleState.println(getQualifiedName() + " has been wiped out!");
    }

    private void setCount(int i) {
        count = i;
    }

    private boolean doOneAttack(BattleState battleState, BattleUnit defender) {
        return MyRandom.rollD10() + combatSkillBonus >= defender.getDefense();
    }

    public String getQualifiedName() {
        return getOrigin() + " " + getName();
    }

    private int getDefense() {
        return defense;
    }

    protected static Sprite[] makeSpriteSet(MyColors color, int row, int offset) {
        Sprite[] result = new Sprite[4];
        for (int i = 0; i < result.length; ++i) {
            int num = 0x10 * row + i + offset;
            result[i] = new Sprite32x32("battleunit" + num, "battle.png", num,
                    MyColors.BLACK, MyColors.GRAY, MyColors.LIGHT_GRAY, color);
        }
        return result;
    }
}
