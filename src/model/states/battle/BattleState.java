package model.states.battle;

import model.Model;
import model.SteppingMatrix;
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

    public BattleState(Model model, KingdomWar war, boolean actAsAggressor) {
        super(model);
        this.terrain = new SteppingMatrix<>(BATTLE_GRID_WIDTH, BATTLE_GRID_HEIGHT);
        terrain.addElement(3, 3, new WoodsBattleTerrain());
        terrain.addElement(4, 4, new HillsBattleTerrain());
        terrain.addElement(5, 5, new DenseWoodsBattleTerrain());

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
        BattleSubView subView = new BattleSubView(terrain, units, this);
        StripedTransition.transition(model, subView); // TODO: Make new transition for this.

        doPlayerTurn(model, subView, playingAggressor ? war.getAggressorUnits() : war.getDefenderUnits());

        return model.getCurrentHex().getEveningState(model, false, false);
    }

    private void doPlayerTurn(Model model, BattleSubView subView, List<BattleUnit> units) {
        MyLists.forEach(units, BattleUnit::refillMovementPoints);
        subView.showMovementPointsForUnits(units);
        do {
            waitForReturnSilently();
            if (!subView.handlePendingBattleAction(model, this)) {
                BattleUnit unit = subView.getUnitUnderCursor();
                if (unit != null) {
                    if ((playingAggressor && war.getAggressorUnits().contains(unit)) ||
                            (!playingAggressor && war.getDefenderUnits().contains(unit))) {
                        subView.setPendingBattleAction(new MoveOrAttackBattleAction(unit));
                    } else {
                        println("You cannot direct the enemy's unit.");
                    }
                } else {
                    subView.cancelPending();
                }
            } else {
                subView.cancelPending();
            }
        } while (!turnIsOver(units));
    }

    private boolean turnIsOver(List<BattleUnit> units) {
        return MyLists.all(units, (BattleUnit bu) -> bu.getMP() == 0);
    }

    public boolean canMoveInDirection(BattleUnit performer, BattleDirection direction) {
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
        return other == null || !other.getOrigin().equals(performer.getOrigin());
    }

    private BattleUnit getOtherUnitInDirection(BattleUnit performer, BattleDirection direction) {
        Point pos = units.getPositionFor(performer);
        Point toPos = new Point(pos.x + direction.dxdy.x, pos.y + direction.dxdy.y);
        return units.getElementAt(toPos.x, toPos.y);
    }

    public void moveOrAttack(Model model, BattleUnit performer, BattleAction action, BattleDirection direction) {
        BattleUnit other = getOtherUnitInDirection(performer, direction);
        if (other == null) {
            print("Move " + performer.getName() + " " + direction.asText + "? (Y/N) ");
            if (yesNoInput()) {
                performer.setMP(performer.getMP() - performer.getMoveCost());
                moveUnitInDirection(performer, direction);
            }
        } else {
            print("Attack " + other.getQualifiedName() + " with " + performer.getName() + "? (Y/N) ");
            if (yesNoInput()) {
                performer.setMP(performer.getMP() - performer.getMoveCost());
                performer.doAttackOn(model, this, other, direction);
            }
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
            if (canMoveInDirection(retreater, dir)) {
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
}
