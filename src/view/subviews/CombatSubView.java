package view.subviews;

import model.*;
import model.characters.GameCharacter;
import model.combat.CombatAction;
import model.combat.Combatant;
import model.enemies.Enemy;
import model.states.CombatEvent;
import model.states.CombatMatrix;
import sprites.CombatCursorSprite;
import util.MyPair;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

public class CombatSubView extends SubView {

    public static final String TITLE_TEXT = "COMBAT";
    private final CombatEvent combat;
    private final CombatMatrix combatMatrix;
    private final CombatTheme theme;
    private Sprite initiativeMarker = new MovingRightArrow(MyColors.WHITE, MyColors.BLACK);
    private Set<MyPair<Point, RunOnceAnimationSprite>> ongoingEffects;

    public CombatSubView(CombatEvent combatEvent, CombatMatrix combatMatrix, CombatTheme theme) {
        this.combat = combatEvent;
        ongoingEffects = new HashSet<>();
        this.combatMatrix = combatMatrix;
        this.theme = theme;
    }


    @Override
    protected String getUnderText(Model model) {
        Combatant selected = combatMatrix.getSelectedElement();
        return selected.getName() + String.format(" %d/%d HP", selected.getHP(), selected.getMaxHP()) ;
    }

    @Override
    protected String getTitleText(Model model) {
        return TITLE_TEXT;
    }

    @Override
    public void drawArea(Model model) {
        ScreenHandler screenHandler = model.getScreenHandler();
        screenHandler.clearSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX);
        theme.drawBackground(model, X_OFFSET, Y_OFFSET);

        drawCombatants(model);
        drawEffects(model);
        drawInitiativeOrder(model);
        drawCursor(model);
    }

    private void drawCursor(Model model) {
        Combatant combatant = combat.getCurrentCombatant();
        Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
        if (combatant != null) {
            cursor = combatant.getCombatCursor(model);
        }
        Point p = new Point(combatMatrix.getSelectedPoint());
        Point p2 = combatMatrix.getSelectedElement().getCursorShift();
        p.y = Y_OFFSET + p2.y + (p.y + 1) * 4 + shiftForCurrent(combatMatrix.getCombatant(p.x, p.y));
        p.x = X_OFFSET + p2.x + p.x * 4;
        model.getScreenHandler().register("combatcursor", p, cursor, 2);
    }

    private synchronized void drawEffects(Model model) {
        List<MyPair<Point, RunOnceAnimationSprite>> toRemove = new ArrayList<>();
        for (MyPair<Point, RunOnceAnimationSprite> p : ongoingEffects) {
            if (!p.second.isDone()) {
                model.getScreenHandler().register(p.second.getName(), p.first, p.second, 2,
                        p.second.getXShift(), -p.second.getYShift());
            } else {
                toRemove.add(p);
            }
        }
        ongoingEffects.remove(toRemove);
    }

    private void drawCombatants(Model model) {
        for (int row = 0; row < combatMatrix.getRows(); ++row) {
            for (int col = 0; col < combatMatrix.getColumns(); ++col) {
                Combatant combatant = combatMatrix.getCombatant(col, row);
                int xpos = X_OFFSET + col*4;
                int ypos = Y_OFFSET + (row+2)*4 + shiftForCurrent(combatant);
                if (combatant != null) {
                    combatant.drawYourself(model.getScreenHandler(), xpos, ypos, getInitiativeSymbol(combatant, model));
                }
            }
        }
    }

    private int shiftForCurrent(Combatant combatant) {
        if (combatant == combat.getCurrentCombatant()) {
            if (combatant instanceof Enemy) {
                return - 1;
            } else {
                return + 1;
            }
        }
        return 0;
    }

    private void drawInitiativeOrder(Model model) {
        int col = 0;
        int xPosStart = X_OFFSET + (X_MAX - X_OFFSET) / 2 - combat.getInitiativeOrder().size();
        for (Combatant combatant : combat.getInitiativeOrder()) {
            if (isCombatantsTurn(combatant)) {
                model.getScreenHandler().put(xPosStart + col - 1, Y_MAX - 1 ,
                        initiativeMarker);
            }
            model.getScreenHandler().put(xPosStart + col, Y_MAX - 1 , getInitiativeSymbol(combatant, model));
            col += 2;
        }
    }

    private boolean isCombatantsTurn(Combatant combatant) {
        if (combatant == combat.getCurrentCombatant()) {
            return true;
        }
        if (combatant instanceof Enemy && combat.getCurrentCombatant() instanceof Enemy) {
            return ((Enemy) combatant).getEnemyGroup() == ((Enemy) combat.getCurrentCombatant()).getEnemyGroup();
        }
        return false;
    }

    private Sprite getInitiativeSymbol(Combatant combatant, Model model) {
        if (model.getParty().getPartyMembers().contains(combatant)) {
            return model.getParty().getInitiativeSymbol(combatant);
        }
        int offset = 0;
        if (combatant instanceof Enemy) {
            offset = ((Enemy) combatant).getEnemyGroup() - 'A';
        }
        return CharSprite.make((char) 0x04 + offset, MyColors.WHITE, MyColors.GRAY, MyColors.BLACK);
    }

    public synchronized void addStrikeEffect(Combatant target, Model model, int damge, boolean critical) {
        Point point = convertToScreen(combatMatrix.getPositionFor(target), target);
        ongoingEffects.add(new MyPair<>(point, new StrikeEffectSprite()));
        ongoingEffects.add(new MyPair<>(point, new DamageValueEffect(damge, critical)));
    }

    private static Point convertToScreen(Point point, Combatant target) {
        Point shiftpoint = target.getCursorShift();
        point.x = X_OFFSET + point.x*4 + shiftpoint.x;
        point.y = Y_OFFSET + (point.y+2)*4 + shiftpoint.y;
        return point;
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        Combatant combatant = combatMatrix.getSelectedElement();
        if (combat.isSelectingFormation()) {
            if (combatMatrix.handleKeyEvent(keyEvent)) {
                return true;
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
                if (combatant instanceof Enemy) {
                    combat.println("Cannot change the formation of an enemy.");
                } else {
                    combat.toggleFormationFor(model, (GameCharacter) combatant);
                }
                return true;
            }
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER && combat.getCurrentCombatant() instanceof GameCharacter) {
            Point point = convertToScreen(combatMatrix.getPositionFor(combatant), combatant);
            List<CombatAction> combatActions = ((GameCharacter) combat.getCurrentCombatant()).getCombatActions(model, combatant, combat);
            CombatActionMenu menu = new CombatActionMenu(model.getSubView(), combatActions, CombatActionMenu.toStringList(combatActions),
                    point.x+3, point.y, DailyActionMenu.NORTH_WEST, combat, combatant, this);
            model.setSubView(menu);
            return true;
        }
        return combatMatrix.handleKeyEvent(keyEvent);
    }

}
