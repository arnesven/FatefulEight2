package view.subviews;

import model.*;
import model.characters.GameCharacter;
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
    private Random random;
    private Sprite treeUpperSprite = new Sprite32x32("treeupper", "combat.png", 0x10, MyColors.DARK_GREEN, MyColors.GREEN, MyColors.CYAN);
    private Sprite treeLowerSprite = new Sprite32x32("treelower", "combat.png", 0x11, MyColors.BROWN, MyColors.DARK_GREEN, MyColors.DARK_GRAY);
    private Sprite grassLineSprite = new Sprite32x32("treelower", "combat.png", 0x12, MyColors.GREEN, MyColors.DARK_GREEN, MyColors.GREEN);
    private Sprite upperContour = new Sprite32x32("treecontourupper", "combat.png", 0x20, MyColors.BLACK, MyColors.DARK_GRAY, MyColors.WHITE);
    private Sprite lowerContour = new Sprite32x32("treecontourlower", "combat.png", 0x21, MyColors.BLACK, MyColors.RED, MyColors.RED);
    private Sprite[] grassSprites = new Sprite[3];
    private Sprite initiativeMarker = new MovingRightArrow(MyColors.WHITE, MyColors.BLACK);
    private Set<MyPair<Point, RunOnceAnimationSprite>> ongoingEffects;

    public CombatSubView(CombatEvent combatEvent, CombatMatrix combatMatrix) {
        this.combat = combatEvent;
        for (int i = 0; i < 3; ++i) {
            grassSprites[i] = new Sprite32x32("grass"+i, "combat.png", 0x13+i, MyColors.GREEN, MyColors.LIGHT_GREEN, MyColors.BEIGE);
        }
        ongoingEffects = new HashSet<>();
        this.combatMatrix = combatMatrix;
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
        drawBackground(model);

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

    private void drawEffects(Model model) {
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
        int offset = ((Enemy)combatant).getEnemyGroup() - 'A';
        return CharSprite.make((char)0x04+offset, MyColors.WHITE, MyColors.GRAY, MyColors.BLACK);
    }

    private void drawBackground(Model model) {
        this.random = new Random(555);
        for (int i = 0; i < 8; ++i) {
            model.getScreenHandler().put(X_OFFSET + i*4, Y_OFFSET, treeUpperSprite);
            model.getScreenHandler().register(upperContour.getName() + i, new Point(X_OFFSET + i*4, Y_OFFSET), upperContour);
            model.getScreenHandler().put(X_OFFSET  + i*4, Y_OFFSET + 4, treeLowerSprite);
            model.getScreenHandler().register(lowerContour.getName() + i, new Point(X_OFFSET + i*4, Y_OFFSET+4), lowerContour);
            model.getScreenHandler().put(X_OFFSET  + i*4, Y_OFFSET + 8, grassLineSprite);
            for (int y= 0; y < 6; y++) {
                model.getScreenHandler().put(X_OFFSET + i*4, Y_OFFSET + (y+3)*4, grassSprites[random.nextInt(grassSprites.length)]);
            }
        }
    }

    public void addStrikeEffect(Combatant target, Model model, int damge, boolean critical) {
        Point point = combatMatrix.getPositionFor(target);
        Point shiftpoint = target.getCursorShift();
        point.x = X_OFFSET + point.x*4 + shiftpoint.x;
        point.y = Y_OFFSET + (point.y+2)*4 + shiftpoint.y;
        ongoingEffects.add(new MyPair<>(point, new StrikeEffectSprite()));
        ongoingEffects.add(new MyPair<>(point, new DamageValueEffect(damge, critical)));
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (combatMatrix.handleKeyEvent(keyEvent)) {
            return true;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE && combat.isSelectingFormation()) {
            Combatant combatant = combatMatrix.getSelectedElement();
            if (combatant instanceof Enemy) {
                combat.println("Cannot change the formation of an enemy.");
            } else {
                combat.toggleFormationFor(model, (GameCharacter)combatant);
            }
            return true;
        }
        return false;
    }

}
