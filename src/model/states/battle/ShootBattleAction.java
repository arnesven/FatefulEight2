package model.states.battle;

import model.Model;
import model.SteppingMatrix;
import view.MyColors;
import view.sprites.CombatCursorSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.awt.event.KeyEvent;

public class ShootBattleAction extends BattleAction {

    private static final Sprite32x32 RANGE_BOX = new Sprite32x32("rangebox", "battle_symbols.png",
            0x20, MyColors.RED, MyColors.DARK_RED, MyColors.WHITE);
    public static final int LONG_RANGE = 6;
    public static final int SHORT_RANGE = 4;
    private final SteppingMatrix<Integer> grid;
    private static final Sprite CURSOR = new CombatCursorSprite(MyColors.RED);

    public ShootBattleAction(BattleUnit unit, BattleState state) {
        super("Shoot", unit);
        Point position = state.getPositionForUnit(unit);
        this.grid = new SteppingMatrix<>(BattleState.BATTLE_GRID_WIDTH, BattleState.BATTLE_GRID_HEIGHT);
        int range = state.getTerrainForPosition(position) instanceof HillsBattleTerrain ? LONG_RANGE : SHORT_RANGE;
        int count = 0;
        for (int y = 0; y < grid.getRows(); y++) {
            for (int x = 0; x < grid.getColumns(); ++x) {
                Point p = new Point(x, y);
                if (p.distance(position) < range && !(position.x == x && position.y == y)) {
                    grid.addElement(x, y, count++);
                    grid.setSelectedPoint(p);
                }
            }
        }
    }

    @Override
    public void execute(Model model, BattleState battleState, BattleUnit performer) {
        battleState.rangedAttack(model, performer, this, grid.getSelectedPoint());
    }

    @Override
    public void drawUnit(Model model, BattleState state, boolean withMp, Point p) {
        super.drawUnit(model, state, withMp, p);
        for (int y = 0; y < grid.getRows(); y++) {
            for (int x = 0; x < grid.getColumns(); ++x) {
                if (grid.getElementAt(x, y) != null) {
                    Point p2 = state.getSubView().convertToScreen(x, y);
                    model.getScreenHandler().register(RANGE_BOX.getName(), p2, RANGE_BOX, 4);
                }
            }
        }
        Point p2 = state.getSubView().convertToScreen(grid.getSelectedPoint().x, grid.getSelectedPoint().y-1);
        model.getScreenHandler().register("combatcursor", p2, CURSOR, 5);
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model, BattleState state) {
        return grid.handleKeyEvent(keyEvent);
    }
}
