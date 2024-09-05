package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.states.RecruitState;
import view.sprites.CombatCursorSprite;
import view.BorderFrame;
import view.MyColors;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Map;

public class RecruitSubView extends TopMenuSubView {
    private final SteppingMatrix<GameCharacter> matrix;
    private final RecruitState state;
    private final Map<GameCharacter, Integer> startingGoldMap;
    private Point cursorPosition;

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
    }

    private void drawCharacter(Model model, GameCharacter gc, int xPos, int yPos) {
        gc.drawAppearance(model.getScreenHandler(), xPos, yPos);
        BorderFrame.drawString(model.getScreenHandler(), gc.getFirstName(), xPos, yPos+7, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), gc.getRace().getName(), xPos, yPos+8, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), gc.getCharClass().getShortName() + " " + gc.getLevel(), xPos, yPos+9, MyColors.WHITE, MyColors.BLUE);
    }

    @Override
    protected String getUnderText(Model model) {
        if (matrix.getSelectedElement() != null) {
            GameCharacter gc = matrix.getSelectedElement();
            int startingGold = 0;
            if (startingGoldMap.containsKey(gc) && state.isStartingGoldEnabled()) {
                startingGold = startingGoldMap.get(gc);
            }
            String text = String.format("%s, %s, %s %d, %s, %d gold", gc.getFullName(), gc.getRace().getName(),
                    gc.getCharClass().getShortName(), gc.getLevel(), gc.getOtherClasses(), startingGold);
            return text;
        }
        return "";
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
}
