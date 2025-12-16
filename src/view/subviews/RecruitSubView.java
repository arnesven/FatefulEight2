package view.subviews;

import model.Model;
import model.PortraitAnimations;
import model.SmallCalloutPortraitAnimations;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.combat.conditions.VampirismCondition;
import model.states.RecruitState;
import view.sprites.CombatCursorSprite;
import view.BorderFrame;
import view.MyColors;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class RecruitSubView extends TopMenuSubView {
    private final SteppingMatrix<GameCharacter> matrix;
    private final RecruitState state;
    private final Map<GameCharacter, Integer> startingGoldMap;
    private Point cursorPosition;
    private PortraitAnimations portraitAnis = new SmallCalloutPortraitAnimations();
    private Map<GameCharacter, Integer> knownInfo = new HashMap<>();

    public RecruitSubView(RecruitState state, SteppingMatrix<GameCharacter> recruitMatrix, Map<GameCharacter, Integer> startingGoldMap) {
        super(2, new int[]{X_OFFSET + 2, X_OFFSET+13, X_OFFSET+24});
        this.state = state;
        this.matrix = recruitMatrix;
        this.startingGoldMap = startingGoldMap;
    }

    @Override
    protected void drawInnerArea(Model model) {
        model.getScreenHandler().fillSpace(X_OFFSET, X_MAX, Y_OFFSET+1, Y_MAX, blueBlock);
        drawAvailableSlots(model);
        drawRecruitables(model);
    }

    private void drawAvailableSlots(Model model) {
        int slots = model.getParty().getInventory().getTentSize() - model.getParty().size();
        MyColors textColor = slots > 0 ? MyColors.WHITE : MyColors.RED;
        BorderFrame.drawCentered(model.getScreenHandler(), "Available tent space: " + slots, Y_OFFSET + 1,
                textColor, MyColors.BLUE);
    }

    @Override
    protected MyColors getTitleColor(Model model, int i) {
        if (i == 0 || i == 2) {
            return MyColors.WHITE;
        }
        return model.getParty().size() > 1 ? MyColors.WHITE : MyColors.GRAY;
    }

    @Override
    protected String getTitle(int i) {
        switch (i) {
            case 0 : return "RECRUIT";
            case 1 : return "DISMISS";
            default : return "EXIT";
        }
    }

    @Override
    protected void drawCursor(Model model) {
        Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
        if (matrix.getSelectedElement() != null) {
            Point p = new Point(matrix.getSelectedPoint());
            p.x = X_OFFSET + p.x * 14 + 6;
            p.y = Y_OFFSET + p.y * 12 - 1;
            model.getScreenHandler().register("recruitcursor", p, cursor, 2);
            this.cursorPosition = p;
        }
    }

    private void drawRecruitables(Model model) {
        for (int col = 0; col < matrix.getColumns(); ++col) {
            for (int row = 0; row < matrix.getRows(); ++row) {
                GameCharacter gc = matrix.getElementAt(col, row);
                if (gc != null) {
                    int xPos = X_OFFSET + col*14 + 5;
                    int yPos = Y_OFFSET + row*12 + 3;
                    drawCharacter(model, gc, xPos, yPos);
                }
            }
        }
        portraitAnis.drawSpeakAnimations(model.getScreenHandler());
    }

    private void drawCharacter(Model model, GameCharacter gc, int xPos, int yPos) {
        gc.drawAppearance(model.getScreenHandler(), xPos, yPos);
        int info = getKnownInfo(gc);
        BorderFrame.drawString(model.getScreenHandler(), gc.getRace().getName(), xPos, yPos+8, MyColors.WHITE, MyColors.BLUE);
        if (info >= 1) {
            BorderFrame.drawString(model.getScreenHandler(), gc.getFirstName(), xPos, yPos + 7, MyColors.WHITE, MyColors.BLUE);
        }
        if (info >= 2) {
            BorderFrame.drawString(model.getScreenHandler(), gc.getCharClass().getShortName() + " " + gc.getLevel(), xPos, yPos + 9, MyColors.WHITE, MyColors.BLUE);
        }
        portraitAnis.drawBlink(model.getScreenHandler(), gc.getAppearance(), new Point(xPos, yPos-3));
    }

    @Override
    protected String getUnderText(Model model) {
        if (matrix.getSelectedElement() != null && knownInfo.containsKey(matrix.getSelectedElement())) {
            GameCharacter gc = matrix.getSelectedElement();
            int startingGold = 0;
            if (startingGoldMap.containsKey(gc) && state.isStartingGoldEnabled()) {
                startingGold = startingGoldMap.get(gc);
            }
            int knownInfo = getKnownInfo(matrix.getSelectedElement());
            if (knownInfo == 1) {
                return String.format("%s", gc.getFullName());
            }
            if (knownInfo == 2) {
                return String.format("%s, %s %d", gc.getFullName(),
                        gc.getCharClass().getShortName(), gc.getLevel());
            }
            if (knownInfo == 3) {
                return String.format("%s, %s %d, %s", gc.getFullName(),
                        gc.getCharClass().getShortName(), gc.getLevel(), gc.getOtherClasses());
            }
            return String.format("%s, %s, %s %d, %s, %d gold", gc.getFullName(), gc.getRace().getName(),
                    gc.getCharClass().getShortName(), gc.getLevel(), gc.getOtherClasses(), startingGold);
        }
        return "???";
    }

    @Override
    protected String getTitleText(Model model) {
        return "RECRUIT";
    }

    @Override
    protected boolean innerHandleKeyEvent(KeyEvent keyEvent, Model model) {
        return matrix.handleKeyEvent(keyEvent);
    }

    @Override
    protected int getDefaultIndex() {
        return 0;
    }

    @Override
    protected boolean cursorOnBorderToTop() {
        return matrix.getSelectedPoint().y == matrix.getMinimumRow();
    }

    public Point getCursorPosition() {
        return cursorPosition;
    }

    public void characterSay(GameCharacter gc, String text) {
        Point p = matrix.getPositionFor(gc);
        int xPos = X_OFFSET + p.x*14 + 5;
        int yPos = Y_OFFSET + p.y*12;
        portraitAnis.addSpeakAnimation(new Point(xPos, yPos), text,
                gc.getAppearance(), gc.hasCondition(VampirismCondition.class));
    }

    public void improveKnownInfo(GameCharacter selected) {
        if (!knownInfo.containsKey(selected)) {
            knownInfo.put(selected, 1);
        } else {
            knownInfo.put(selected, knownInfo.get(selected) + 1);
        }
    }

    public int getKnownInfo(GameCharacter selected) {
        if (!knownInfo.containsKey(selected)) {
            return 0;
        }
        return knownInfo.get(selected);
    }
}
