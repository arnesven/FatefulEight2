package view.subviews;

import model.*;
import model.characters.GameCharacter;
import model.combat.abilities.CombatAction;
import model.combat.Combatant;
import model.enemies.Enemy;
import model.states.CombatEvent;
import model.states.CombatMatrix;
import util.MyLists;
import view.BorderFrame;
import view.combat.CombatTheme;
import view.sprites.CombatCursorSprite;
import util.MyPair;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CombatSubView extends SubView {

    public static final String TITLE_TEXT = "COMBAT";
    public static final int BLOCK_TEXT = 1;
    public static final int EVADE_TEXT = 0;
    public static final int MISS_TEXT = 2;
    public static final int PARRY_TEXT = 3;
    private static final Sprite FIREWALL_SPRITE = new FirewallSprite();
    private final CombatEvent combat;
    private final CombatMatrix combatMatrix;
    private final CombatTheme theme;
    public static final Sprite INITIATIVE_MARKER = new MovingRightArrow(MyColors.WHITE, MyColors.BLACK);
    public static final Sprite CURRENT_MARKER = new QuestCursorSprite();
    private int splashAnimationCountDown = 0;
    private String splash = "";
    private boolean isFleeing = false;
    private final Map<Combatant, Sprite> fleeingAvatars = new HashMap<>();

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
        if (combat.isSurprise()) {
            return TITLE_TEXT + " - SURPRISE";
        }
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
        drawFlameWall(model);
        drawInitiativeOrder(model);
        drawRoundCounter(model);
        drawCursor(model);
    }

    private void drawRoundCounter(Model model) {
        if (splashAnimationCountDown > 0) {
            BorderFrame.drawCentered(model.getScreenHandler(), splash,
                    Y_OFFSET  + (Y_MAX - Y_OFFSET) / 2 + 2, MyColors.WHITE, MyColors.BLACK);
            splashAnimationCountDown--;
        }
    }

    private void drawCursor(Model model) {
        if (isFleeing) {
            return;
        }
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
                    if (combatant instanceof GameCharacter && isFleeing && !combatant.isDead()) {
                        Sprite avatarBack;
                        if (fleeingAvatars.containsKey(combatant)) {
                            avatarBack = fleeingAvatars.get(combatant);
                        } else {
                            avatarBack = ((GameCharacter) combatant).getAvatarSprite().getAvatarBack();
                            fleeingAvatars.put(combatant, avatarBack);
                        }
                        model.getScreenHandler().register(avatarBack.getName(), new Point(xpos, ypos), avatarBack);
                    } else {
                        combatant.drawYourself(model.getScreenHandler(), xpos, ypos, getInitiativeSymbol(combatant, model));
                        if (combatant == combat.getCurrentCombatant()) {
                            model.getScreenHandler().register(CURRENT_MARKER.getName(), new Point(xpos, ypos), CURRENT_MARKER);
                        }
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
        List<Combatant> initOrder = new ArrayList<>(combat.getInitiativeOrder());
        if (initOrder.size() >= (X_MAX - X_OFFSET) / 2) {
            drawOverflowingInitOrder(model, initOrder);
        } else {
            drawCenteredInitOrder(model, initOrder);
        }
    }

    private void drawOverflowingInitOrder(Model model, List<Combatant> initOrder) {
        int xPos = X_OFFSET + 1;
        int overflowBy = initOrder.size() - ((X_MAX - X_OFFSET) / 2);
        int combatantIndex = initOrder.indexOf(MyLists.find(initOrder, this::isCombatantsTurn));
        int numberToSkip = Math.min(overflowBy, combatantIndex);
        for (Combatant combatant : initOrder) {
            if (numberToSkip > 0) {
                numberToSkip--;
            } else {
                if (isCombatantsTurn(combatant)) {
                    model.getScreenHandler().put(xPos - 1, Y_MAX - 1,
                            INITIATIVE_MARKER);
                }
                model.getScreenHandler().put(xPos, Y_MAX - 1, getInitiativeSymbol(combatant, model));
                xPos += 2;
            }
            if (xPos >= X_MAX) {
                break;
            }
        }
        if (combatantIndex < overflowBy) {
            model.getScreenHandler().put(X_MAX-1, Y_MAX - 1, CharSprite.make((char)0x7E, MyColors.WHITE));
        }
    }

    private void drawCenteredInitOrder(Model model, List<Combatant> initOrder) {
        int col = 0;
        int xPosStart = X_OFFSET + (X_MAX - X_OFFSET) / 2 - combat.getInitiativeOrder().size();
        for (Combatant combatant : initOrder) {
            if (isCombatantsTurn(combatant)) {
                model.getScreenHandler().put(xPosStart + col - 1, Y_MAX - 1 ,
                        INITIATIVE_MARKER);
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

    public static Sprite getInitiativeSymbol(Combatant combatant, Model model) {
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

    public synchronized void addFloatyDamage(Combatant target, int damge, MyColors color) {
        if (combatMatrix.getElementList().contains(target)) {
            Point point = convertToScreen(combatMatrix.getPositionFor(target), target);
            super.addFloatyDamage(point, damge, color);
        }
    }

    public synchronized void addFloatyText(Combatant target, int strikeTextEffect) {
        if (combatMatrix.getElementList().contains(target)) {
            Point point = convertToScreen(combatMatrix.getPositionFor(target), target);
            super.addFloatyText(point, strikeTextEffect);
        }
    }

    public synchronized void addSpecialEffect(Combatant target, RunOnceAnimationSprite sprite) {
        if (combatMatrix.getElementList().contains(target)) {
            Point point = convertToScreen(combatMatrix.getPositionFor(target), target);
            addOngoingEffect(new MyPair<>(point, sprite));
        }
    }

    public synchronized void addSpecialEffectsBetween(Combatant from, Combatant to, RunOnceAnimationSprite sprite) {
        if (combatMatrix.getElementList().contains(from) && combatMatrix.getElementList().contains(to)) {
            Point fromPoint = convertToScreen(combatMatrix.getPositionFor(from), from);
            Point toPoint = convertToScreen(combatMatrix.getPositionFor(to), to);

            double angle = Math.toDegrees(Math.atan2(fromPoint.y - toPoint.y, fromPoint.x - toPoint.x));
            System.out.println("Rotation angle " + angle);
            angle += 90.0;
            while (angle < 0.0) {
                angle += 360.0;
            }
            sprite.setRotation(angle);

            double distance = toPoint.distance(fromPoint);
            Point diff = new Point(toPoint.x - fromPoint.x, toPoint.y - fromPoint.y);
            Point2D.Double delta = new Point2D.Double(diff.x / (distance / 4), diff.y / (distance / 4));
            for (int i = 0; i < (int)(distance / 4); i++) {
                int finalX = (int)(fromPoint.x + i * delta.x + delta.x / 2);
                int finalY = (int)(fromPoint.y + i * delta.y + delta.y / 2);
                addOngoingEffect(new MyPair<>(new Point(finalX, finalY), sprite));
            }
        }
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
                List<CombatAction> combatActions = CombatAction.getCombatActions(model,
                        (GameCharacter)combat.getCurrentCombatant(), combatant, combat);
                if (combatant instanceof GameCharacter) {
                    combatActions.removeIf((CombatAction ca) -> ca.getName().equals("Attack"));
                }
                int anchor = DailyActionMenu.NORTH_WEST;
                int y = point.y;
                if (combatant instanceof Enemy) {
                    anchor = DailyActionMenu.SOUTH_WEST;
                    y = point.y + 4;
                }
                CombatActionMenu menu = new CombatActionMenu(model.getSubView(), combatActions, CombatActionMenu.toStringList(combatActions),
                        point.x + 3, y, anchor, combat, combatant, this);
                model.setSubView(menu);
                return true;
            }
        }
        return combatMatrix.handleKeyEvent(keyEvent);
    }


    private void drawFlameWall(Model model) {
        if (combat.hasFlameWall()) {
            for (int x = 0; x < 8; ++x) {
                Point point = new Point();
                point.x = X_OFFSET + x * 4;
                point.y = Y_OFFSET + 4 * 4;
                model.getScreenHandler().register(FIREWALL_SPRITE.getName(), point, FIREWALL_SPRITE);
            }
        }
    }

    public void displaySplashMessage(String splashMessage) {
        this.splash = splashMessage;
        this.splashAnimationCountDown = 300;
    }

    public void enableFleeingAnimation() {
        this.isFleeing = true;
    }

    public void addSpecialEffectEverywhere(List<Combatant> excluding, RunOnceAnimationSprite animationSprite) {
        for (int y = 0; y < combatMatrix.getRows(); ++y) {
            for (int x = 0; x < combatMatrix.getColumns(); ++x) {
                Combatant elem = combatMatrix.getElementAt(x, y);
                if (elem == null || !excluding.contains(elem)) {
                    Point p = new Point(X_OFFSET + x*4, Y_OFFSET + (y+2)*4);
                    addOngoingEffect(new MyPair<>(p, animationSprite));
                }
            }
        }
    }
}
