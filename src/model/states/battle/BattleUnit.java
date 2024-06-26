package model.states.battle;

import model.Model;
import util.MyRandom;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.sprites.Sprite8x8;

import java.awt.*;
import java.io.Serializable;

public abstract class BattleUnit implements Serializable {

    private static final Sprite8x8[] MP_SPRITES = makeMPSprites();
    private static final int DEFAULT_TURN_COST = 1;
    private final String name;
    private final int maximumMP;
    private int currentMP;
    private int count;
    private final String origin;
    private BattleDirection direction = BattleDirection.east;
    private final int combatSkillBonus;
    private final int defense;

    public BattleUnit(String name, int count, int combatSkillBonus, int defense, int movementPoints, String origin) {
        this.name = name;
        this.count = count;
        this.combatSkillBonus = combatSkillBonus;
        this.defense = defense;
        this.maximumMP = movementPoints;
        this.origin = origin;
        this.currentMP = movementPoints;
    }

    protected abstract Sprite[] getSprites();

    public void drawYourself(ScreenHandler screenHandler, Point p, boolean withMp, int prio) {
        Sprite spr = getSprites()[direction.value];
        screenHandler.register(spr.getName(), p, spr, prio);
        if (withMp) {
            Sprite mpSprite = MP_SPRITES[currentMP];
            Point p2 = new Point(p.x + 3, p.y + 3);
            screenHandler.register(mpSprite.getName(), p2, mpSprite, prio+1);
        }
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
        battleState.print(getQualifiedName() + " attack " + defender.getQualifiedName());
        int flankOrRearBonus = checkForFlankOrRear(battleState, defender, attackDirection);
        int highGroundBonus = checkForHighGroundBonus(battleState, defender);

        int hits = 0;
        for (int i = 0; i < getCount(); ++i) {
            hits += doOneAttack(battleState, defender, flankOrRearBonus, 0) ? 1 : 0;
        }
        int counterHits = 0;
        for (int i = 0; i < defender.getCount(); ++i) {
            counterHits += defender.doOneAttack(battleState, this, 0, highGroundBonus) ? 1 : 0;
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

    private int checkForHighGroundBonus(BattleState battleState, BattleUnit defender) {
        BattleTerrain terrain = battleState.getTerrainForPosition(battleState.getPositionForUnit(defender));
        if (terrain != null) {
            return terrain.checkForMeleeDefenseBonus(battleState, defender);
        }
        return 0;
    }

    private int checkForFlankOrRear(BattleState battleState, BattleUnit defender, BattleDirection attackDirection) {
        if (attackDirection == defender.getDirection()) {
            battleState.println("- Rear attack! (+2 to attack)");
            return 2;
        }
        if (attackDirection == defender.getDirection().getOpposite()) {
            battleState.println(".");
            return 0;
        }
        battleState.println("- Flank attack! (+1 to attack)");
        return 1;
    }

    public void wipeOut(BattleState battleState) {
        setCount(0);
        battleState.removeUnit(this);
        battleState.println(getQualifiedName() + " has been wiped out!");
    }

    private void setCount(int i) {
        count = i;
    }

    private boolean doOneAttack(BattleState battleState, BattleUnit defender, int flankOrRearBonus, int defenseBonus) {
        return MyRandom.rollD10() + combatSkillBonus + flankOrRearBonus >= defender.getDefense() + defenseBonus;
    }

    public String getQualifiedName() {
        return getOrigin() + " " + getName();
    }

    private int getDefense() {
        return defense;
    }

    protected static Sprite[] makeSpriteSet(int row, int offset, MyColors color1, MyColors color2, MyColors color3, MyColors color4) {
        Sprite[] result = new Sprite[4];
        for (int i = 0; i < result.length; ++i) {
            int num = 0x10 * row + i + offset;
            result[i] = new Sprite32x32("battleunit" + num, "battle.png", num,
                    color1, color2, color3, color4);
        }
        return result;
    }

    public void refillMovementPoints() {
        this.currentMP = maximumMP;
    }


    private static Sprite8x8[] makeMPSprites() {
        Sprite8x8[] result = new Sprite8x8[16];
        for (int i = 0; i < result.length; ++i) {
            MyColors bgColor = i == 0 ? MyColors.LIGHT_RED : MyColors.LIGHT_GREEN;
            Sprite8x8 spr = new Sprite8x8("mpsprite"+i, "battle_symbols.png", i,
                    bgColor, bgColor, MyColors.BLACK, MyColors.GOLD);
            result[i] = spr;
        }
        return result;
    }

    public int getMP() {
        return currentMP;
    }

    protected void setMP(int mp) {
        currentMP = mp;
    }

    public int getTurnCost() {
        return DEFAULT_TURN_COST;
    }
}
