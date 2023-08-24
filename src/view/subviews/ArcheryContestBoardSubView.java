package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import view.BorderFrame;
import view.MyColors;
import view.sprites.ArrowSprites;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class ArcheryContestBoardSubView extends TopMenuSubView {
    private final SteppingMatrix<GameCharacter> matrix;

    public ArcheryContestBoardSubView(List<GameCharacter> contestants) {
        super(2, new int[]{X_OFFSET + 12});
        this.matrix = new SteppingMatrix<>(1, contestants.size());
        int row = 0;
        for (GameCharacter chara : contestants) {
            matrix.addElement(0, row++, chara);
        }
    }

    @Override
    protected String getUnderText(Model model) {
        return "The contestants of the archery contest.";
    }

    @Override
    protected String getTitleText(Model model) {
        return "ARCHERY CONTEST";
    }

    @Override
    protected void drawCursor(Model model) {
        Point pos = new Point(matrix.getSelectedPoint());
        pos.x += X_OFFSET;
        pos.y += Y_OFFSET + 3;
        model.getScreenHandler().put(pos.x, pos.y, ArrowSprites.RIGHT);
    }

    @Override
    protected void drawInnerArea(Model model) {
        model.getScreenHandler().fillSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX, blueBlock);
        BorderFrame.drawString(model.getScreenHandler(), "CONTESTANT          R1  R2  R3",
                X_OFFSET + 1, Y_OFFSET + 2, MyColors.WHITE, MyColors.BLUE);
        for (int i = 0; i < matrix.getElementList().size(); ++i) {
            BorderFrame.drawString(model.getScreenHandler(), matrix.getElementAt(0, i).getName(),
                    X_OFFSET + 1, Y_OFFSET + 4 + i, MyColors.WHITE, MyColors.BLUE);
        }

        drawSelectedContestant(model);
    }

    private void drawSelectedContestant(Model model) {
        Point pos = new Point(X_OFFSET + 4, Y_MAX-10);
        model.getScreenHandler().clearSpace(X_OFFSET, X_MAX, pos.y, Y_MAX);
        GameCharacter fighter = matrix.getSelectedElement();
        BorderFrame.drawString(model.getScreenHandler(), fighter.getName(), pos.x, pos.y, MyColors.LIGHT_GRAY);
        String raceAndClassString = fighter.getRace().getName() + " " + fighter.getCharClass().getShortName() + " Lvl " + fighter.getLevel();
        BorderFrame.drawString(model.getScreenHandler(), raceAndClassString, pos.x, pos.y+1, MyColors.LIGHT_GRAY);
        fighter.getAppearance().drawYourself(model.getScreenHandler(), pos.x, pos.y + 3);
        fighter.getEquipment().drawYourself(model.getScreenHandler(), pos.x, pos.y);
    }

    @Override
    protected MyColors getTitleColor(Model model, int i) {
        return MyColors.WHITE;
    }

    @Override
    protected String getTitle(int i) {
        return "DONE";
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
}
