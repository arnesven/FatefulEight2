package model.states.battle;

import model.Model;
import model.SteppingMatrix;
import model.map.wars.KingdomWar;
import model.states.GameState;
import sound.BackgroundMusic;
import sound.ClientSoundManager;
import view.subviews.BattleSubView;
import view.subviews.StripedTransition;

import javax.swing.text.Position;
import java.awt.*;
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
            bu.setDirection(BattleUnit.Direction.north);
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
            bu.setDirection(BattleUnit.Direction.south);
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
        } while (!battleIsOver());

        return model.getCurrentHex().getEveningState(model, false, false);
    }

    private boolean battleIsOver() {
        return false;
    }

    public boolean canMoveInDirection(BattleUnit performer, BattleUnit.Direction direction) {
        Point pos = units.getPositionFor(performer);
        if (direction == BattleUnit.Direction.east && pos.x == BATTLE_GRID_WIDTH-1) {
            return false;
        } else if (direction == BattleUnit.Direction.west && pos.x == 0) {
            return false;
        } else if (direction == BattleUnit.Direction.north && pos.y == 0) {
            return false;
        } else if (direction == BattleUnit.Direction.south && pos.y == BATTLE_GRID_HEIGHT-1) {
            return false;
        }
        BattleUnit other = getOtherUnitInDirection(performer, direction);
        return other == null || !other.getOrigin().equals(performer.getOrigin());
    }

    private BattleUnit getOtherUnitInDirection(BattleUnit performer, BattleUnit.Direction direction) {
        Point pos = units.getPositionFor(performer);
        Point toPos = new Point(pos.x + direction.dxdy.x, pos.y + direction.dxdy.y);
        return units.getElementAt(toPos.x, toPos.y);
    }

    public void moveOrAttack(Model model, BattleUnit performer, BattleAction action, BattleUnit.Direction direction) {
        BattleUnit other = getOtherUnitInDirection(performer, direction);
        if (other == null) {
            print("Move " + performer.getName() + " " + direction.asText + "? (Y/N) ");
            if (yesNoInput()) {
                Point pos = units.getPositionFor(performer);
                Point toPos = new Point(pos.x + direction.dxdy.x, pos.y + direction.dxdy.y);
                units.remove(performer);
                units.addElement(toPos.x, toPos.y, performer);
            }
        } else {
            print("Attack " + other.getOrigin() + " " + other.getName() + " with " + performer.getName() + "? (Y/N) ");
            yesNoInput();
            //TODO : Resolve attack
        }
    }
}
