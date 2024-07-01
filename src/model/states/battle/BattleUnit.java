package model.states.battle;

import model.Model;
import util.MyRandom;
import view.GameView;
import view.MyColors;
import view.ScreenHandler;
import view.help.HelpDialog;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.sprites.Sprite8x8;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

public abstract class BattleUnit implements Serializable {

    private static final Sprite8x8[] MP_SPRITES = makeMPSprites();
    private static final int DEFAULT_TURN_COST = 1;
    public static final int ROUT_THRESHOLD = 4;
    public static final int REAR_ATTACK_BONUS = 2;
    public static final int FLANK_ATTACK_BONUS = 1;
    protected static MyColors UNIFORM_COLOR = MyColors.BEIGE;
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

    public void refillMovementPoints() {
        this.currentMP = maximumMP;
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

    public void drawYourself(ScreenHandler screenHandler, Point p, boolean withMp, int prio, int mpToUse) {
        Sprite spr = getSprites()[direction.value];
        screenHandler.register(spr.getName(), p, spr, prio);
        if (withMp) {
            Sprite mpSprite = MP_SPRITES[mpToUse];
            Point p2 = new Point(p.x + 3, p.y + 3);
            screenHandler.register(mpSprite.getName(), p2, mpSprite, prio+1);
        }
    }

    public String getName() {
        return name;
    }

    public String getQualifiedName() {
        return getOrigin() + " " + getName();
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

    public void wipeOut(BattleState battleState) {
        setCount(0);
        battleState.removeUnit(this);
        battleState.println(getQualifiedName() + " have been wiped out!");
    }

    public void doAttackOn(Model model, BattleState battleState, BattleUnit defender, BattleDirection attackDirection) {
        int attackBonus = checkForFlankOrRear(battleState, defender, attackDirection);
        attackBonus += this.getSpecificVSAttackBonusWhenAttacking(battleState, defender);
        int defenseBonus = checkForHighGroundBonus(battleState, defender) +
                defender.getSpecificVSDefenseBonusWhenDefending(battleState, this);
        int hits = 0;
        for (int i = 0; i < getCount(); ++i) {
            hits += doOneAttack(battleState, defender, attackBonus, defenseBonus) ? 1 : 0;
        }

        int counterHits = 0;
        if (this.hasFirstStrike()) {
            battleState.println("Attacker has First Strike, does " + hits + " hits.");
            defender.takeCasualties(battleState, hits);
            if (defender.getCount() > 0) {
                counterHits = defenderCounterAttack(battleState, defender);
            }
        } else {
            counterHits = defenderCounterAttack(battleState, defender);
            battleState.println("Attacker does " + hits + " hits, defender does " + counterHits + " hits.");
            defender.takeCasualties(battleState, hits);
        }

        this.takeCasualties(battleState, counterHits);

        boolean attackerWins = defender.getCount() == 0 || hits > counterHits;
        if (getCount() > 0) {
            if (attackerWins) {
                if (defender.getCount() > 0) {
                    battleState.retreatUnit(defender, attackDirection);
                }
                if (!defender.checkForRout(battleState)) {
                    battleState.moveUnitInDirection(this, attackDirection);
                }
            } else {
                battleState.println("Attacker has lost the fight and must retreat back to its original space.");
                defender.setDirection(attackDirection.getOpposite());
            }
        }
    }

    private boolean checkForRout(BattleState battleState) {
        if (hasLowMorale() && 0 < getCount() && getCount() < ROUT_THRESHOLD) {
            battleState.println(getQualifiedName() + " has been routed and flees the battlefield!");
            battleState.removeUnit(this);
            return true;
        }
        return false;
    }

    protected boolean hasLowMorale() {
        return false;
    }

    protected int getSpecificVSDefenseBonusWhenDefending(BattleState battleState, BattleUnit attacker) {
        return 0;
    }

    protected int getSpecificVSAttackBonusWhenDefending(BattleState battleState, BattleUnit attacker) {
        return 0;
    }

    protected int getSpecificVSAttackBonusWhenAttacking(BattleState battleState, BattleUnit defender) {
        return 0;
    }

    protected int getSpecificVSDefenseBonusWhenAttacking(BattleState battleState, BattleUnit defender) {
        return 0;
    }

    protected boolean hasFirstStrike() {
        return false;
    }

    private void takeCasualties(BattleState battleState, int hits) {
        if (getCount() <= hits) {
            this.wipeOut(battleState);
        } else {
            setCount(getCount() - hits);
        }
    }

    private int defenderCounterAttack(BattleState battleState, BattleUnit defender) {
        int defenseBonus = checkForHighGroundBonus(battleState, this);
        defenseBonus += this.getSpecificVSDefenseBonusWhenAttacking(battleState, defender);
        int attackBonus = defender.getSpecificVSAttackBonusWhenDefending(battleState, this);
        int counterHits = 0;
        for (int i = 0; i < defender.getCount(); ++i) {
            counterHits += defender.doOneAttack(battleState, this, attackBonus, defenseBonus) ? 1 : 0;
        }
        return counterHits;
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
            battleState.println("Rear attack! (+" + REAR_ATTACK_BONUS + " to attack)");
            return 2;
        }
        if (attackDirection == defender.getDirection().getOpposite()) {
            return 0;
        }
        battleState.println("Flank attack! (+" + FLANK_ATTACK_BONUS + " to attack)");
        return 1;
    }

    private void setCount(int i) {
        count = i;
    }

    private boolean doOneAttack(BattleState battleState, BattleUnit defender, int attackBonus, int defenseBonus) {
        return MyRandom.rollD10() + combatSkillBonus + attackBonus >= defender.getDefense() + defenseBonus;
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

    public List<BattleAction> getBattleActions(BattleState battleState) {
        return List.of(new MoveOrAttackBattleAction(this));
    }

    public void doRangedAttackOn(BattleState battleState, BattleUnit target) {
        BattleTerrain terrain = battleState.getTerrainForPosition(battleState.getPositionForUnit(target));
        int targetCoverBonus = 0;
        if (terrain != null) {
            targetCoverBonus = terrain.getCoverDefenseBonus(battleState);
        }
        int hits = 0;
        for (int i = 0; i < getCount(); ++i) {
            hits += doOneAttack(battleState, target, 0, targetCoverBonus) ? 1 : 0;
        }
        battleState.println("Ranged attack does " + hits + " hits.");
        target.takeCasualties(battleState, hits);
    }

    protected static MyColors fixColor(MyColors color) {
        if (color == MyColors.WHITE) {
            return MyColors.GRAY;
        } if (color == MyColors.YELLOW) {
            return MyColors.GOLD;
        }
        return color;
    }

    public abstract HelpDialog getHelpSection(GameView view);

    public String getStatsString() {
        return "Movement Points: " + maximumMP + "\n" +
               "Combat Skill:    " + combatSkillBonus + "\n" +
               "Defense:         " + defense;
    }
}
