package model.states.battle;

import model.Model;
import model.SteppingMatrix;
import model.map.CastleLocation;
import model.map.wars.KingdomWar;
import model.states.GameState;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import util.MyLists;
import view.subviews.BattleSubView;
import view.subviews.StripedTransition;

import java.awt.*;
import java.util.ArrayList;
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

    public BattleState(Model model, KingdomWar war, boolean actAsAggressor) {
        super(model);
        this.terrain = new SteppingMatrix<>(BATTLE_GRID_WIDTH, BATTLE_GRID_HEIGHT);
        terrain.addElement(2, 2, new WaterBattleTerrain());
        terrain.addElement(0, 6, new HillsBattleTerrain());
        terrain.addElement(3, 3, new WoodsBattleTerrain());
        terrain.addElement(4, 4, new HillsBattleTerrain());
        terrain.addElement(5, 5, new DenseWoodsBattleTerrain());
        terrain.addElement(6, 4, new SwampBattleTerrain());

        this.units = new SteppingMatrix<>(BATTLE_GRID_WIDTH, BATTLE_GRID_HEIGHT);
        this.playingAggressor = actAsAggressor;
        this.war = war;
        if (actAsAggressor) {
            placeUnitsSouth(war.getAggressorUnits());
            placeUnitsNorth(war.getDefenderUnits());
        } else {
            placeUnitsNorth(war.getAggressorUnits());
            placeUnitsSouth(war.getDefenderUnits());
        }
    }

    private void placeUnitsSouth(List<BattleUnit> unitList) {
        int col = 0;
        int row = BATTLE_GRID_HEIGHT-2;
        for (BattleUnit bu : unitList) {
            bu.setDirection(BattleDirection.north);
            units.addElement(col++, row, bu);
            if (col == BATTLE_GRID_WIDTH) {
                col = 0;
                row++;
            }
        }
    }

    private void placeUnitsNorth(List<BattleUnit> unitList) {
        int col = BATTLE_GRID_WIDTH-1;
        int row = 1;
        for (BattleUnit bu : unitList) {
            bu.setDirection(BattleDirection.south);
            units.addElement(col--, row, bu);
            if (col == -1) {
                col = BATTLE_GRID_WIDTH-1;
                row--;
            }
        }
    }

    @Override
    public GameState run(Model model) {
        ClientSoundManager.playBackgroundMusic(BackgroundMusic.combatSong);
        this.subView = new BattleSubView(terrain, units, this);
        StripedTransition.transition(model, subView); // TODO: Make new transition for this.

        model.getTutorial().battles(model);

        List<BattleUnit> playerUnits = playingAggressor ? war.getAggressorUnits() : war.getDefenderUnits();
        List<BattleUnit> opponentUnits = playingAggressor ? war.getDefenderUnits() : war.getAggressorUnits();

        boolean abandoned = false;
        while (!(allVanquished(playerUnits) || allVanquished(opponentUnits))) {
            println("Your turn.");
            abandoned = !doPlayerTurn(model, playerUnits);
            if (abandoned || allVanquished(opponentUnits) || allVanquished(playerUnits)) {
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
        } else {
            println("You have been defeated in battle.");
        }

        return model.getCurrentHex().getEveningState(model, false, false);
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

    private BattleUnit getOtherUnitInDirection(BattleUnit performer, BattleDirection direction) {
        Point pos = units.getPositionFor(performer);
        Point toPos = new Point(pos.x + direction.dxdy.x, pos.y + direction.dxdy.y);
        return units.getElementAt(toPos.x, toPos.y);
    }

    public void moveOrAttack(Model model, BattleUnit performer, BattleAction action, BattleDirection direction) {
        BattleUnit other = getOtherUnitInDirection(performer, direction);
        int moveCost = MovePointCostForDestination(performer, direction);
        if (other == null) {
            if (!action.isNoPrompt()) {
                print("Move " + performer.getName() + " " + direction.asText + "? (Y/N) ");
            } else {
                delay(200);
            }
            if (action.isNoPrompt() || yesNoInput()) {
                performer.setMP(performer.getMP() - moveCost);
                moveUnitInDirection(performer, direction);
            }
        } else {
            if (!action.isNoPrompt()) {
                print("Attack " + other.getQualifiedName() + " with " + performer.getName() + "? (Y/N) ");
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
            if (canMoveInDirection(retreater, dir, false)) {
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

    public int MovePointCostForDestination(BattleUnit performer, BattleDirection newDirection) {
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
            print("Make ranged attack on " + other.getQualifiedName() + " with " + performer.getName() + "? (Y/N) ");
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
}
