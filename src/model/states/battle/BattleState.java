package model.states.battle;

import model.GameStatistics;
import model.Model;
import model.SteppingMatrix;
import model.map.CastleLocation;
import model.map.wars.KingdomWar;
import model.states.CombatEvent;
import model.states.GameState;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import util.MyLists;
import util.MyPair;
import util.MyRandom;
import view.subviews.BattleSubView;
import view.subviews.SnakeTransition;
import view.subviews.StripedTransition;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BattleState extends GameState {
    public static final int BATTLE_GRID_WIDTH = 8;
    public static final int BATTLE_GRID_HEIGHT = 9;
    private final SteppingMatrix<BattleTerrain> terrain;
    private final SteppingMatrix<BattleUnit> units;
    private final boolean playingAggressor;
    private final KingdomWar war;
    private final BattleAI opponentAI = new ImprovedBattleAI();
    private BattleSubView subView;
    private boolean wasVictorious = false;
    private int turnCount = 0;

    public BattleState(Model model, KingdomWar war, boolean actAsAggressor) {
        super(model);
        this.terrain = new SteppingMatrix<>(BATTLE_GRID_WIDTH, BATTLE_GRID_HEIGHT);
        for (MyPair<Point, BattleTerrain> terr : war.getTerrains()) {
            terrain.addElement(terr.first.x, terr.first.y, terr.second);
        }

        this.units = new SteppingMatrix<>(BATTLE_GRID_WIDTH, BATTLE_GRID_HEIGHT);
        this.playingAggressor = actAsAggressor;
        this.war = war;
        if (actAsAggressor) {
            placeUnitsNorth(war.getAggressorUnits());
            placeUnitsSouth(war.getDefenderUnits());
        } else {
            placeUnitsSouth(war.getAggressorUnits());
            placeUnitsNorth(war.getDefenderUnits());
        }
    }

    @Override
    public GameState run(Model model) {
        ClientSoundManager.playBackgroundMusic(BackgroundMusic.battleSong);
        GameStatistics.incrementBattlesFought();
        this.subView = new BattleSubView(terrain, units, this, war.getGroundColor());
        SnakeTransition.transition(model, subView);

        model.getTutorial().battles(model);
        List<BattleUnit> playerUnits = playingAggressor ? war.getAggressorUnits() : war.getDefenderUnits();
        List<BattleUnit> opponentUnits = playingAggressor ? war.getDefenderUnits() : war.getAggressorUnits();

        boolean abandoned = false;
        while (!battleIsOver(playerUnits, opponentUnits)) {
            turnCount++;
            println("Your turn.");
            abandoned = !doPlayerTurn(model, playerUnits);
            if (abandoned || battleIsOver(playerUnits, opponentUnits)) {
                break;
            }
            String sideName = CastleLocation.placeNameShort(playingAggressor ? war.getDefender() : war.getAggressor());
            println(sideName + "'s turn.");
            doAITurn(model, opponentUnits);
        }

        if (abandoned) {
            println("You abandoned the battle.");
        } else if (allVanquished(opponentUnits)) {
            println("You are victorious!");
            wasVictorious = true;
        } else {
            println("You have been defeated in battle.");
        }
        print("Press enter to continue.");
        waitForReturn();
        return model.getCurrentHex().getEveningState(model, false, false);
    }

    private boolean battleIsOver(List<BattleUnit> playerUnits, List<BattleUnit> opponentUnits) {
        return allVanquished(playerUnits) || allVanquished(opponentUnits) || opponentRetreats(playerUnits, opponentUnits);
    }

    private boolean opponentRetreats(List<BattleUnit> playerUnits, List<BattleUnit> opponentUnits) {
        if (turnCount > 2 && MyLists.intAccumulate(playerUnits, BattleUnit::getCount) >
            MyLists.intAccumulate(opponentUnits, BattleUnit::getCount) * 3) {
            if (MyRandom.flipCoin()) {
                println("The enemy has retreated from battle!");
                for (BattleUnit bu : opponentUnits) {
                    if (units.getElementList().contains(bu)) {
                        units.remove(bu);
                    }
                }
                return true;
            }
        }
        return false;
    }

    public boolean wasVictorious() {
        return wasVictorious;
    }

    private boolean allVanquished(List<BattleUnit> opponentUnits) {
        return !MyLists.any(opponentUnits, (BattleUnit bu) -> units.getElementList().contains(bu));
    }

    private void doAITurn(Model model, List<BattleUnit> opponentUnits) {
        MyLists.forEach(opponentUnits, BattleUnit::refillMovementPoints);
        subView.showMovementPointsForUnits(opponentUnits);
        opponentAI.startTurn(model, subView, this, opponentUnits);
        do {
            opponentAI.activateUnit(model, subView, this);
        } while (!opponentAI.isDone());
    }

    private boolean doPlayerTurn(Model model, List<BattleUnit> playerUnits) {
        MyLists.forEach(playerUnits, BattleUnit::refillMovementPoints);
        subView.showMovementPointsForUnits(playerUnits);
        do {
            waitForReturnSilently();
            if (subView.cursorIsOnQuit()) {
                return false;
            }
            if (subView.cursorIsOnDone()) {
                return true;
            }
            if (!subView.handlePendingBattleAction(model, this)) {
                BattleUnit unit = subView.getUnitUnderCursor();
                if (unit != null && unit.getMP() > 0) {
                    if (playerUnits.contains(unit)) {
                        List<BattleAction> listOfActions = unit.getBattleActions(this);
                        BattleAction selectedBattleAction = null;
                        if (listOfActions.size() > 1) {
                            List<String> actionNames = MyLists.transform(listOfActions, BattleAction::getName);
                            Point p = units.getPositionFor(unit);
                            p = subView.convertToScreen(p.x, p.y);
                            int selected = multipleOptionArrowMenu(model, p.x, p.y, actionNames);
                            selectedBattleAction = listOfActions.get(selected);
                        } else {
                            selectedBattleAction = listOfActions.get(0);
                        }
                        subView.setPendingBattleAction(selectedBattleAction);
                    } else {
                        println("You cannot direct the enemy's unit.");
                    }
                } else {
                    subView.cancelPending();
                }
            } else {
                subView.cancelPending();
            }
        } while (!turnIsOver(playerUnits));
        return true;
    }

    private boolean turnIsOver(List<BattleUnit> units) {
        return MyLists.all(units, (BattleUnit bu) -> bu.getMP() == 0) || allVanquished(units);
    }

    public boolean canMoveInDirection(BattleUnit performer, BattleDirection direction,
                                      boolean allowMoveIntoEnemyUnit) {
        Point pos = units.getPositionFor(performer);
        if (direction == BattleDirection.east && pos.x == BATTLE_GRID_WIDTH-1) {
            return false;
        } else if (direction == BattleDirection.west && pos.x == 0) {
            return false;
        } else if (direction == BattleDirection.north && pos.y == 0) {
            return false;
        } else if (direction == BattleDirection.south && pos.y == BATTLE_GRID_HEIGHT-1) {
            return false;
        }
        BattleUnit other = getOtherUnitInDirection(performer, direction);
        if (other == null) {
            return true;
        }
        if (!allowMoveIntoEnemyUnit) {
            return false;
        }
        return !other.getOrigin().equals(performer.getOrigin());
    }

    public BattleUnit getOtherUnitInDirection(BattleUnit performer, BattleDirection direction) {
        Point pos = units.getPositionFor(performer);
        Point toPos = new Point(pos.x + direction.dxdy.x, pos.y + direction.dxdy.y);
        return units.getElementAt(toPos.x, toPos.y);
    }

    public void moveOrAttack(Model model, BattleUnit performer, BattleAction action, BattleDirection direction) {
        BattleUnit other = getOtherUnitInDirection(performer, direction);
        int moveCost = movePointCostForDestination(performer, direction);
        if (other == null) {
            if (action.isNoPrompt()) {
                delay(200);
            }
            performer.setMP(performer.getMP() - moveCost);
            moveUnitInDirection(performer, direction);
        } else {
            if (!action.isNoPrompt()) {
                print("Attack " + other.getQualifiedName() + " with " + performer.getName() + " (uses all remaining MP)? (Y/N) ");
            } else {
                println(performer.getQualifiedName() + " attack " + other.getQualifiedName() + "!");
                delay(200);
            }
            if (action.isNoPrompt() || yesNoInput()) {
                subView.startDustCloudAnimation(units.getPositionFor(other), List.of(performer, other));
                performer.doAttackOn(model, this, other, direction);
                model.getLog().waitForAnimationToFinish();
                subView.removeDustCloudAnimation();
                performer.setMP(0);
            }
        }
    }

    public void delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void moveUnitInDirection(BattleUnit performer, BattleDirection direction) {
        Point pos = units.getPositionFor(performer);
        Point toPos = new Point(pos.x + direction.dxdy.x, pos.y + direction.dxdy.y);
        units.remove(performer);
        units.addElement(toPos.x, toPos.y, performer);
    }

    public void removeUnit(BattleUnit unit) {
        units.remove(unit);
    }

    public void retreatUnit(BattleUnit retreater, BattleDirection attackDirection) {
        println("Defender has lost the fight and must retreat.");
        List<BattleDirection> candidates =
                new ArrayList<>(List.of(BattleDirection.east, BattleDirection.north,
                        BattleDirection.south, BattleDirection.south));
        candidates.remove(attackDirection);
        candidates.remove(attackDirection.getOpposite());
        candidates.add(attackDirection);
        BattleDirection retreatDirection = null;
        for (BattleDirection dir : candidates) {
            if (canMoveInDirection(retreater, dir, false) &&
                    movePointCostForDestination(retreater, dir) < BattleTerrain.IMPASSIBLE_TERRAIN_MOVE_COST) {
                retreatDirection = dir;
                break;
            }
        }

        if (retreatDirection == null) {
            println("Nowhere to retreat, unit is eliminated!");
            retreater.wipeOut(this);
        } else {
            moveUnitInDirection(retreater, retreatDirection);
            if (retreatDirection == attackDirection) {
                retreater.setDirection(retreatDirection.getOpposite());
            } else {
                retreater.setDirection(retreatDirection);
            }
        }
    }

    public Point getPositionForUnit(BattleUnit activeUnit) {
        return units.getPositionFor(activeUnit);
    }

    public BattleTerrain getTerrainForPosition(Point pos) {
        return terrain.getElementAt(pos.x, pos.y);
    }

    public int movePointCostForDestination(BattleUnit performer, BattleDirection newDirection) {
        Point performerPos = new Point(units.getPositionFor(performer));
        performerPos.translate(newDirection.dxdy.x, newDirection.dxdy.y);
        BattleTerrain destinationTerrain = terrain.getElementAt(performerPos.x, performerPos.y);
        if (destinationTerrain == null) {
            return BattleTerrain.DEFAULT_MOVE_COST;
        }
        return destinationTerrain.getMoveCost();
    }

    public BattleSubView getSubView() {
        return subView;
    }

    public void rangedAttack(Model model, BattleUnit performer, BattleAction action, Point targetPoint) {
        BattleUnit other = units.getElementAt(targetPoint.x, targetPoint.y);
        if (other == null) {
            return;
        }
        if (other.getOrigin().equals(performer.getOrigin())) {
            println("You can't attack your own unit.");
            return;
        }
        if (!action.isNoPrompt()) {
            print("Make ranged attack on " + other.getQualifiedName() + " with " + performer.getName() + " (uses all remaining MP)? (Y/N) ");
        } else {
            println(performer.getQualifiedName() + " make a ranged attack on " + other.getQualifiedName() + "!");
            delay(200);
        }
        if (action.isNoPrompt() || yesNoInput()) {
            subView.doRangedAttackAnimation(performer, targetPoint);
            performer.doRangedAttackOn(this, other);
            performer.setMP(0);
        }
    }

    public List<BattleUnit> getOpposingUnits(BattleUnit currentUnit) {
        if (war.getAggressorUnits().contains(currentUnit)) {
            return war.getDefenderUnits();
        }
        return war.getAggressorUnits();
    }

    public BattleUnit getUnitForPosition(Point current) {
        return units.getElementAt(current.x, current.y);
    }

    private void placeUnitsSouth(List<BattleUnit> unitList) {
        placeUnits(unitList, makePositions(BATTLE_GRID_HEIGHT-2, +1), BattleDirection.north);
    }

    private void placeUnitsNorth(List<BattleUnit> unitList) {
        placeUnits(unitList, makePositions(1, -1), BattleDirection.south);
    }

    private void placeUnits(List<BattleUnit> unitList, List<Point> positions, BattleDirection facing) {
        for (BattleUnit bu : unitList) {
            if (bu.getCount() > 0) {
                bu.setDirection(facing);
                if (positions.isEmpty()) {
                    System.err.println("Warning, to many battle units to place!");
                    break;
                }
                Point toPlaceAt = positions.remove(0);
                units.addElement(toPlaceAt.x, toPlaceAt.y, bu);
            }
        }
    }

    private List<Point> makePositions(int rowStart, int toAdd) {
        List<Point> positions1 = new ArrayList<>();
        for (int i = 0; i < BATTLE_GRID_WIDTH; ++i) {
            positions1.add(new Point(i, rowStart));
        }
        List<Point> positions2 = new ArrayList<>();
        for (int i = 0; i < BATTLE_GRID_WIDTH; ++i) {
            positions2.add(new Point(i, rowStart + toAdd));
        }
        Collections.shuffle(positions1);
        Collections.shuffle(positions2);

        List<Point> positions = new ArrayList<>();
        positions.addAll(positions1);
        positions.addAll(positions2);
        return positions;
    }

    public boolean isOnBattlefield(BattleUnit currentUnit) {
        return units.getElementList().contains(currentUnit);
    }
}
