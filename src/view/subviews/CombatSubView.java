package view.subviews;

import model.*;
import model.characters.GameCharacter;
import model.combat.CombatAction;
import model.combat.Combatant;
import model.enemies.Enemy;
import model.states.CombatEvent;
import model.states.CombatMatrix;
import view.sprites.CombatCursorSprite;
import util.MyPair;
import util.MyRandom;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class CombatSubView extends SubView {

    public static final String TITLE_TEXT = "COMBAT";
    public static final int BLOCK_TEXT = 1;
    public static final int EVADE_TEXT = 0;
    public static final int MISS_TEXT = 2;
    private final CombatEvent combat;
    private final CombatMatrix combatMatrix;
    private final CombatTheme theme;
    private final Sprite initiativeMarker = new MovingRightArrow(MyColors.WHITE, MyColors.BLACK);
    private final Sprite currentMarker = new QuestCursorSprite();

    public CombatSubView(CombatEvent combatEvent, CombatMatrix combatMatrix, CombatTheme theme) {
        this.combat = combatEvent;
        this.combatMatrix = combatMatrix;
        this.theme = theme;
    }


    @Override
    protected String getUnderText(Model model) {
        Combatant selected = combatMatrix.getSelectedElement();
        String status = selected.getStatus();
        if (status.equals("OK")) {
            status = "";
        } else {
            status = ", " + status;
        }
        if (selected instanceof Enemy) {
            if (!((Enemy) selected).getAttackBehavior().getUnderText().equals("")) {
                status += ", " + ((Enemy) selected).getAttackBehavior().getUnderText() + " Attack";
            }
        }
        return selected.getName() + String.format(" %d/%d HP", selected.getHP(), selected.getMaxHP()) + status;
    }

    @Override
    protected String getTitleText(Model model) {
        if (combat.isAmbush()) {
            return TITLE_TEXT + " - AMBUSH";
        }
        return TITLE_TEXT;
    }

    @Override
    public void drawArea(Model model) {
        ScreenHandler screenHandler = model.getScreenHandler();
        screenHandler.clearSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX);
        theme.drawBackground(model, X_OFFSET, Y_OFFSET);

        drawCombatants(model);
        drawInitiativeOrder(model);
        drawCursor(model);
    }

    private void drawCursor(Model model) {
        Combatant combatant = combat.getCurrentCombatant();
        Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
        if (combatant != null) {
            if (model.getParty().getPartyMembers().contains(combatant)) {
                cursor = combatant.getCombatCursor(model);
            }
        }
        Point p = new Point(combatMatrix.getSelectedPoint());
        Point p2 = combatMatrix.getSelectedElement().getCursorShift();
        p.y = Y_OFFSET + p2.y + (p.y + 1) * 4 + shiftForCurrent(combatMatrix.getCombatant(p.x, p.y));
        p.x = X_OFFSET + p2.x + p.x * 4;
        model.getScreenHandler().register("combatcursor", p, cursor, 2);
    }

    protected void drawCombatants(Model model) {
        for (int row = 0; row < combatMatrix.getRows(); ++row) {
            for (int col = 0; col < combatMatrix.getColumns(); ++col) {
                Combatant combatant = combatMatrix.getCombatant(col, row);
                int xpos = X_OFFSET + col*4;
                int ypos = Y_OFFSET + (row+2)*4 + shiftForCurrent(combatant);
                if (combatant != null) {
                    combatant.drawYourself(model.getScreenHandler(), xpos, ypos, getInitiativeSymbol(combatant, model));
                    if (combatant == combat.getCurrentCombatant()) {
                        model.getScreenHandler().register(currentMarker.getName(), new Point(xpos, ypos), currentMarker);
                    }
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
        if (combatant instanceof GameCharacter) { // Ally
            return CharSprite.make((char)0x00, MyColors.WHITE, MyColors.GRAY, MyColors.BLACK);
        }
        int offset = 0;
        if (combatant instanceof Enemy) {
            offset = ((Enemy) combatant).getEnemyGroup() - 'A';
        }
        return CharSprite.make((char) 0x04 + offset, MyColors.WHITE, MyColors.GRAY, MyColors.BLACK);
    }

    public synchronized void addFloatyDamage(Combatant target, int damge, boolean critical) {
        Point point = convertToScreen(combatMatrix.getPositionFor(target), target);
        point.x += MyRandom.randInt(-1, 1);
        point.y += MyRandom.randInt(-1, 1);
        //addOngoingEffect(new MyPair<>(point, new StrikeEffectSprite()));
        if (damge > 15) {
            Point left = new Point(point);
            left.x -= 1;
            addOngoingEffect(new MyPair<>(left, new DamageValueEffect(damge/10, critical)));
            addOngoingEffect(new MyPair<>(point, new DamageValueEffect(damge, critical)));
        } else {
            addOngoingEffect(new MyPair<>(point, new DamageValueEffect(damge, critical)));
        }
    }

    public synchronized void addFloatyText(Combatant target, int strikeTextEffect) {
        Point point = convertToScreen(combatMatrix.getPositionFor(target), target);
        int mapOffset = 0xF0 + strikeTextEffect*3;
        for (int x = 0; x < 3; ++x) {
            addOngoingEffect(new MyPair<>(new Point(point.x + x - 1, point.y),
                    new DamageValueEffect(mapOffset + x)));
        }
    }

    public synchronized void addSpecialEffect(Combatant target, RunOnceAnimationSprite sprite) {
        Point point = convertToScreen(combatMatrix.getPositionFor(target), target);
        if (target instanceof Enemy) {
            point.x -= (target.getWidth() / 2) * 4;
        }
        addOngoingEffect(new MyPair<>(point, sprite));
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
                } else if (model.getParty().getPartyMembers().contains(combatant)) {
                    combat.toggleFormationFor(model, (GameCharacter) combatant);
                } else {
                    combat.println("Cannot change the formation of an ally.");
                }
                return true;
            }
        }
        if (!combat.playerHasSelectedAction()) {
            if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER && combat.getCurrentCombatant() instanceof GameCharacter) {
                Point point = convertToScreen(combatMatrix.getPositionFor(combatant), combatant);
                List<CombatAction> combatActions = ((GameCharacter) combat.getCurrentCombatant()).getCombatActions(model, combatant, combat);
                if (combatant instanceof GameCharacter) {
                    combatActions.removeIf((CombatAction ca) -> ca.getName().equals("Attack"));
                }
                CombatActionMenu menu = new CombatActionMenu(model.getSubView(), combatActions, CombatActionMenu.toStringList(combatActions),
                        point.x + 3, point.y, DailyActionMenu.NORTH_WEST, combat, combatant, this);
                model.setSubView(menu);
                return true;
            }
        }
        return combatMatrix.handleKeyEvent(keyEvent);
    }

}
