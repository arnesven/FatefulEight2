package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.states.battle.*;
import view.MyColors;
import view.combat.CombatTheme;
import view.sprites.CombatCursorSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class BattleSubView extends SubView {

    private final Sprite[] groundSprites;
    private final SteppingMatrix<BattleTerrain> terrain;
    private final SteppingMatrix<Integer> grid;
    private final SteppingMatrix<BattleUnit> units;
    private final BattleState state;
    private BattleAction pendingBattleAction = null;
    private List<BattleUnit> unitsToShowMpFor = new ArrayList<>();

    public BattleSubView(SteppingMatrix<BattleTerrain> terrain, SteppingMatrix<BattleUnit> units, BattleState state) {
        this.terrain = terrain;
        this.units = units;
        groundSprites = CombatTheme.makeGroundSprites(MyColors.GREEN, MyColors.LIGHT_GREEN, 1);
        grid = makeGrid();
        this.state = state;
    }

    @Override
    protected void drawArea(Model model) {
        drawGround(model);
        drawTerrain(model);
        drawUnits(model);
        drawCursor(model);
    }

    private void drawUnits(Model model) {
        for (int y = 0; y < units.getRows(); ++y) {
            for (int x = 0; x < units.getColumns(); ++x) {
                if (units.getElementAt(x, y) != null) {
                    BattleUnit unit = units.getElementAt(x, y);
                    Point p = convertToScreen(x, y);
                    boolean withMovementPoints = unitsToShowMpFor.contains(unit);
                    if (pendingBattleAction != null && pendingBattleAction.getPerformer() == unit) {
                        pendingBattleAction.drawUnit(model, state, withMovementPoints, p);
                    } else {
                        unit.drawYourself(model.getScreenHandler(), p, withMovementPoints, 2);
                    }
                }
            }
        }
    }

    private void drawCursor(Model model) {
        Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
        Point p = convertToScreen(grid.getSelectedPoint().x, grid.getSelectedPoint().y-1);
        model.getScreenHandler().register("combatcursor", p, cursor, 5);
    }

    private void drawTerrain(Model model) {
        for (int y = 0; y < terrain.getRows(); ++y) {
            for (int x = 0; x < terrain.getColumns(); ++x) {
                if (terrain.getElementAt(x, y) != null) {
                    BattleTerrain loc = terrain.getElementAt(x, y);
                    Point p = convertToScreen(x, y);
                    loc.drawYourself(model.getScreenHandler(), p);
                }
            }
        }
    }

    private Point convertToScreen(int x, int y) {
        return new Point(X_OFFSET + x*4, Y_OFFSET + y*4);
    }

    private void drawGround(Model model) {
        for (int i = 0; i < 8; ++i) {
            for (int y= 0; y < 9; y++) {
                model.getScreenHandler().put(X_OFFSET + i*4, Y_OFFSET + y*4, groundSprites[4]);
            }
        }
    }

    @Override
    protected String getUnderText(Model model) {
        Point cursor = grid.getSelectedPoint();
        BattleTerrain terr = terrain.getElementAt(cursor.x, cursor.y);

        BattleUnit unit = units.getElementAt(cursor.x, cursor.y);
        if (unit != null) {
            String text = unit.getOrigin() + " " + unit.getName() + " (" + unit.getCount() + ") " + unit.getMP() + " MP left.";
            if (terr != null) {
                return text + " in " + terr.getName();
            }
            return text;
        }

        if (terr != null) {
            return terr.getName();
        }
        return "";
    }

    @Override
    protected String getTitleText(Model model) {
        return "BATTLE";
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (pendingBattleAction != null) {
            if (pendingBattleAction.handleKeyEvent(keyEvent, model, state)) {
                return true;
            }
        }
        return grid.handleKeyEvent(keyEvent);
    }

    private static SteppingMatrix<Integer> makeGrid() {
        SteppingMatrix<Integer> grid = new SteppingMatrix<>(BattleState.BATTLE_GRID_WIDTH, BattleState.BATTLE_GRID_HEIGHT);
        for (int i = grid.getColumns() * grid.getRows(); i > 0; --i) {
            grid.addElementLast(i);
        }
        grid.setSelectedElement(40);
        return grid;
    }

    public BattleUnit getUnitUnderCursor() {
        return units.getElementAt(grid.getSelectedPoint().x, grid.getSelectedPoint().y);
    }

    public void setPendingBattleAction(BattleAction action) {
        this.pendingBattleAction = action;
    }

    public boolean handlePendingBattleAction(Model model, BattleState battleState) {
        if (pendingBattleAction == null) {
            return false;
        }
        pendingBattleAction.execute(model, battleState, pendingBattleAction.getPerformer());
        if (pendingBattleAction.getPerformer().getCount() > 0) {
            grid.setSelectedPoint(units.getPositionFor(pendingBattleAction.getPerformer()));
        }
        return true;
    }

    public void cancelPending() {
        pendingBattleAction = null;
    }

    public void showMovementPointsForUnits(List<BattleUnit> units) {
        this.unitsToShowMpFor = units;
    }
}
