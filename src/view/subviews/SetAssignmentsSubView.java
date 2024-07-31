package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.characters.GameCharacter;
import model.headquarters.GiveAssignmentsHeadquartersAction;
import model.headquarters.Headquarters;
import util.Arithmetics;
import util.MyStrings;
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
        super(2, new int[]{X_OFFSET + 1, X_OFFSET + 13, X_OFFSET + 24});
        this.hq = headquarters;
        this.matrix = new SteppingMatrix<>(2, headquarters.getMaxCharacters() / 2);
        matrix.addElements(headquarters.getCharacters());
    }

    @Override
    protected void drawCursor(Model model) {
        Point p = matrix.getSelectedPoint();
        Point p2 = new Point(convertToScreen(p.x, p.y+1));
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
                    Point p = convertToScreen(x, y+1);
                    AvatarSprite asp = gc.getAvatarSprite();
                    p.y += 2;
                    asp.synch();
                    model.getScreenHandler().register(asp.getName(), p, asp);
                    BorderFrame.drawString(model.getScreenHandler(), getAssignmentString(gc),
                            p.x + 4 , p.y + 2, MyColors.WHITE, MyColors.BLUE);
                }
            }
        }

        Point p = convertToScreen(0, 0);
        int row = p.y+2;
        BorderFrame.drawString(model.getScreenHandler(), "Food limit: " + hq.getFoodLimit(), p.x, row++, MyColors.WHITE, MyColors.BLUE);

        if (GiveAssignmentsHeadquartersAction.canAssignSubParty(model)) {
            BorderFrame.drawString(model.getScreenHandler(), "Sub-party trip: " +
                            Headquarters.getTripLengthString(hq.getTripLength()),
                    p.x, row++, MyColors.WHITE, MyColors.BLUE);
        }
    }

    @Override
    protected MyColors getTitleColor(Model model, int i) {
        if (i == 1 && !GiveAssignmentsHeadquartersAction.canAssignSubParty(model)) {
            return MyColors.GRAY;
        }
        return MyColors.WHITE;
    }

    @Override
    protected String getTitle(int i) {
        if (i == 0) {
            return "FOOD";
        }
        if (i == 1) {
            return "TRIP";
        }
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
            return "Set food limit for shopping";
        }
        if (this.getTopIndex() == 1) {
            return "Set trip length for sub-party";
        }
        if (this.getTopIndex() == 2) {
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
        return 2;
    }

}
