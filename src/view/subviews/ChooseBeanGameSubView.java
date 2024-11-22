package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.states.beangame.BeanGameBoard;
import view.BorderFrame;
import view.MyColors;
import view.sprites.CombatCursorSprite;
import view.sprites.Sprite;

import javax.swing.text.Position;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class ChooseBeanGameSubView extends SubView {
    private final List<Sprite> miniSprites;
    private final SteppingMatrix<BeanGameBoard> matrix = new SteppingMatrix<>(3, 1);
    private boolean selectEnabled = false;

    public ChooseBeanGameSubView(List<BeanGameBoard> boards) {
        this.miniSprites = new ArrayList<>();
        for (BeanGameBoard bgs : boards) {
            miniSprites.add(bgs.makeMiniSprite());
        }
        System.out.println("Minisprites loaded!");
        matrix.addElements(boards);
    }

    @Override
    protected void drawArea(Model model) {
        int xOffset = 2;
        int index = 0;
        for (Sprite sp : miniSprites) {
            int x = X_OFFSET+xOffset;
            int y = Y_OFFSET+2;
            model.getScreenHandler().register(sp.getName(), new Point(x, y+2), sp);
            BorderFrame.drawString(model.getScreenHandler(), matrix.getElementAt(index, 0).getName(),
                    x, y, MyColors.WHITE);
            xOffset += sp.getWidth() / 8 + 3;
            index++;
        }

        if (selectEnabled && matrix.getSelectedPoint() != null) {
            Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
            Point p = new Point(matrix.getSelectedPoint());
            p.x = X_OFFSET + 3 + p.x * 10;
            p.y = Y_OFFSET + p.y * 4 - 2;
            model.getScreenHandler().register("recruitcursor", p, cursor, 2);
        }
    }

    @Override
    protected String getUnderText(Model model) {
        return "Choose Bean Game Board.";
    }

    @Override
    protected String getTitleText(Model model) {
        return "EVENT - BEAN GAME";
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        return matrix.handleKeyEvent(keyEvent);
    }

    public BeanGameBoard getSelectedBoard() {
        return matrix.getSelectedElement();
    }

    public void enableSelect() {
        selectEnabled = true;
    }
}
