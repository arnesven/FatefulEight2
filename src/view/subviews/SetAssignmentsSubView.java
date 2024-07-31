package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.headquarters.Headquarters;
import view.BorderFrame;
import view.MyColors;
import view.sprites.AvatarSprite;
import view.sprites.CombatCursorSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;

public class SetAssignmentsSubView extends TopMenuSubView {
    private final Headquarters hq;
    private final SteppingMatrix<GameCharacter> matrix;
    private final Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;

    public SetAssignmentsSubView(Headquarters headquarters) {
        super(2, new int[]{X_OFFSET + 12});
        this.hq = headquarters;
        this.matrix = new SteppingMatrix<>(2, headquarters.getMaxCharacters() / 2);
        matrix.addElements(headquarters.getCharacters());

    }

    @Override
    protected void drawCursor(Model model) {
        Point p = matrix.getSelectedPoint();
        Point p2 = new Point(convertToScreen(p.x, p.y));
        p2.y -= 2;
        model.getScreenHandler().register(cursor.getName(), p2, cursor);
    }

    @Override
    protected void drawInnerArea(Model model) {
        model.getScreenHandler().fillSpace(X_OFFSET, X_MAX, Y_OFFSET, Y_MAX,
                blueBlock);
        for (int y = 0; y < matrix.getRows(); ++y) {
            for (int x = 0; x < matrix.getColumns(); ++x) {
                GameCharacter gc = matrix.getElementAt(x, y);
                if (gc != null) {
                    Point p = convertToScreen(x, y);
                    AvatarSprite asp = gc.getAvatarSprite();
                    p.y += 2;
                    asp.synch();
                    model.getScreenHandler().register(asp.getName(), p, asp);
                    BorderFrame.drawString(model.getScreenHandler(), getAssignmentString(gc),
                            p.x + 4 , p.y + 2, MyColors.WHITE, MyColors.BLUE);
                }
            }
        }
    }

    @Override
    protected MyColors getTitleColor(Model model, int i) {
        return MyColors.WHITE;
    }

    @Override
    protected String getTitle(int i) {
        return "RETURN";
    }

    private String getAssignmentString(GameCharacter gc) {
        if (hq.getTownWorkers().contains(gc)) {
            return "Town Work";
        }
        if (hq.getShoppers().contains(gc)) {
            return "Shopping";
        }
        if (hq.getSubParty().contains(gc)) {
            return "Sub-Party";
        }
        return "R'n'R";
    }

    private Point convertToScreen(int x, int y) {
        return new Point(X_OFFSET + x * 16, Y_OFFSET + y * 4);
    }

    @Override
    protected String getUnderText(Model model) {
        if (this.getTopIndex() == 0) {
            return "Return to top menu";
        }
        GameCharacter chara = getSelectedCharacter();
        return chara.getName() + ", " + chara.getRace().getName() + " " +
                chara.getCharClass().getShortName() + " Lvl " + chara.getLevel();
    }

    public GameCharacter getSelectedCharacter() {
        return matrix.getSelectedElement();
    }

    @Override
    protected String getTitleText(Model model) {
        return "HEADQUARTERS - ASSIGNMENTS";
    }

    @Override
    protected boolean cursorOnBorderToTop() {
        return matrix.getSelectedPoint().y == matrix.getMinimumRow();
    }

    @Override
    protected boolean innerHandleKeyEvent(KeyEvent keyEvent, Model model) {
        return matrix.handleKeyEvent(keyEvent);
    }

    @Override
    protected int getDefaultIndex() {
        return 0;
    }

}
