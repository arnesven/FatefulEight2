package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.states.battle.*;
import view.MyColors;
import view.combat.CombatTheme;
import view.sprites.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class BattleSubView extends SubView {

    private static final Sprite32x16 DONE_BUTTON_SPRITE = makeButtonSprite(0);
    private static final Sprite32x16 QUIT_BUTTON_SPRITE = makeButtonSprite(1);
    private LoopingSprite dustCloudSprite;
    private final Sprite[] groundSprites;
    private final SteppingMatrix<BattleTerrain> terrain;
    private final SteppingMatrix<Integer> grid;
    private final SteppingMatrix<BattleUnit> units;
    private final BattleState state;
    private BattleAction pendingBattleAction = null;
    private List<BattleUnit> unitsToShowMpFor = new ArrayList<>();
    private Point dustCloudAnimationPos = null;
    private List<BattleUnit> unitsNotToDraw = new ArrayList<>();

    private Point2D.Double shootingPosition = null;
    private Point2D.Double shootingTarget = null;
    private Point2D.Double shootingDxDy = null;
    private Sprite shootingSpriteToUse = null;

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
        if (pendingBattleAction == null) {
            drawCursor(model);
        }
        drawButtons(model);
        drawShootingEffect(model);
    }

    private void drawButtons(Model model) {
        Point p = convertToScreen(3, 9);
        model.getScreenHandler().put(p.x, p.y, DONE_BUTTON_SPRITE);
        model.getScreenHandler().put(p.x+4, p.y, QUIT_BUTTON_SPRITE);
    }

    private void drawUnits(Model model) {
        for (int y = 0; y < units.getRows(); ++y) {
            for (int x = 0; x < units.getColumns(); ++x) {
                boolean isDustCloudSpace = isDrawingDustCloud() && dustCloudAnimationPos.x == x && dustCloudAnimationPos.y == y;
                if (isDustCloudSpace) {
                    Point p = convertToScreen(dustCloudAnimationPos.x, dustCloudAnimationPos.y);
                    model.getScreenHandler().register(dustCloudSprite.getName(), p, dustCloudSprite);
                }
                if (units.getElementAt(x, y) != null) {
                    BattleUnit unit = units.getElementAt(x, y);
                    if (!unitsNotToDraw.contains(unit)) {
                        Point p = convertToScreen(x, y);
                        boolean withMovementPoints = unitsToShowMpFor.contains(unit);
                        if (pendingBattleAction != null && pendingBattleAction.getPerformer() == unit) {
                            pendingBattleAction.drawUnit(model, state, withMovementPoints, p);
                        } else {
                            unit.drawYourself(model.getScreenHandler(), p, withMovementPoints, 2, unit.getMP());
                        }
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

    public Point convertToScreen(int x, int y) {
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
        if (cursorIsOnDone()) {
            return "End your turn.";
        }
        if (cursorIsOnQuit()) {
            return "Abandon the battle.";
        }
        BattleTerrain terr = terrain.getElementAt(cursor.x, cursor.y);

        BattleUnit unit = units.getElementAt(cursor.x, cursor.y);
        if (unit != null) {
            String text = unit.getOrigin() + " " + unit.getName() + " (" + unit.getCount() + ") " + unit.getMP() + " MP left,";
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
        SteppingMatrix<Integer> grid = new SteppingMatrix<>(BattleState.BATTLE_GRID_WIDTH, BattleState.BATTLE_GRID_HEIGHT+1);
        for (int i = grid.getColumns() * (grid.getRows()-1); i > 0; --i) {
            grid.addElementLast(i);
        }
        grid.setSelectedElement(44);
        grid.addElement(3, grid.getRows()-1, 0);
        grid.addElement(4, grid.getRows()-1, -1);
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


    private static Sprite32x16 makeButtonSprite(int offset) {
        Sprite32x16 result = new Sprite32x16("battledonebutton", "battle_symbols.png", 0x10 + offset);
        result.setColor1(MyColors.WHITE);
        result.setColor2(MyColors.WHITE);
        result.setColor3(MyColors.BLUE);
        return result;
    }

    public boolean cursorIsOnDone() {
        return grid.getSelectedElement() == 0;
    }

    public boolean cursorIsOnQuit() {
        return grid.getSelectedElement() == -1;
    }

    public void startDustCloudAnimation(Point position, List<BattleUnit> unitsNotToDraw) {
        dustCloudSprite = new LoopingSprite("dustcloud", "battle_symbols.png", 0x10, 32, 32);
        dustCloudSprite.setFrames(4);
        dustCloudSprite.setDelay(8);
        dustCloudSprite.setColor1(MyColors.GRAY);
        dustCloudSprite.setColor2(MyColors.LIGHT_GRAY);
        dustCloudSprite.setColor3(MyColors.BEIGE);
        dustCloudSprite.setColor4(MyColors.WHITE);
        this.dustCloudAnimationPos = position;
        this.unitsNotToDraw = unitsNotToDraw;
    }

    public void removeDustCloudAnimation() {
        AnimationManager.unregister(dustCloudSprite);
        this.dustCloudAnimationPos = null;
        this.unitsNotToDraw = new ArrayList<>();
    }

    private boolean isDrawingDustCloud() {
        return this.dustCloudAnimationPos != null;
    }

    public void doRangedAttackAnimation(BattleUnit performer, Point targetPoint) {
        Point p = state.getPositionForUnit(performer);
        Point from = convertToScreen(p.x, p.y);
        this.shootingPosition = new Point2D.Double(from.x*8.0, from.y*8.0);
        Point to = convertToScreen(targetPoint.x, targetPoint.y);
        this.shootingTarget = new Point2D.Double(to.x*8, to.y*8);
        double distance = shootingTarget.distance(shootingPosition);
        this.shootingDxDy = new Point2D.Double(
                (shootingTarget.x - shootingPosition.x) / distance,
                (shootingTarget.y - shootingPosition.y) / distance);

        this.shootingSpriteToUse = new Sprite32x32("shootingtouse", "battle_symbols.png", 0x21,
                MyColors.BLACK, MyColors.DARK_RED, MyColors.WHITE);

        double angle = Math.toDegrees(Math.atan2(from.y - to.y, from.x - to.x));
        System.out.println("Rotation angle " + angle);
        angle += 90.0;
        while (angle < 0.0) { // TODO: Something wrong with the angles here...
            angle += 360.0;
        }
        System.out.println("Setting rotation " + angle);
        shootingSpriteToUse.setRotation(angle);

    }

    private void drawShootingEffect(Model model) {
        if (shootingPosition == null) {
            return;
        }
        double distance = shootingPosition.distance(shootingTarget);
        if (distance < 16.0) {
            shootingPosition = null;
            shootingTarget = null;
            shootingDxDy = null;
            this.shootingSpriteToUse.setRotation(0);
            this.shootingSpriteToUse = null;
        } else {
            double speed = 8.0;
            shootingPosition = new Point2D.Double(
                    shootingDxDy.x * speed + shootingPosition.x,
                    shootingDxDy.y * speed + shootingPosition.y);

            Point screenPosition = new Point((int)Math.floor(shootingPosition.x / 8.0),
                                             (int)Math.floor(shootingPosition.y / 8.0));
            int xShift = (int)shootingPosition.x % 8;
            int yShift = (int)shootingPosition.y % 8;
            model.getScreenHandler().register(shootingSpriteToUse.getName(), screenPosition,
                    shootingSpriteToUse, 4, xShift, yShift);
        }
    }
}
