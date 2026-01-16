package view.subviews;


import model.Model;
import model.SteppingMatrix;
import model.mainstory.jungletribe.PyramidPuzzleSubView;
import model.mainstory.jungletribe.QanoiDisc;
import model.mainstory.jungletribe.QanoiPin;
import util.MyPair;
import view.sprites.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;


public class QanoiPuzzleSubView extends PyramidPuzzleSubView {

    private static final Sprite[] FACIT_SPRITES = makeFacitSprites();

    private static Sprite[] makeFacitSprites() {
        Sprite[] result = new Sprite16x16[]{
                new Sprite16x16("facit1", "quest.png", 0x1E9),
                new Sprite16x16("facit1", "quest.png", 0x1F9),
                new Sprite16x16("facit1", "quest.png", 0x1F8),
                new Sprite16x16("facit1", "quest.png", 0x1E8),
        };
        return result;
    }

    private final SteppingMatrix<QanoiPin> pins;
    private final MyPair<List<Integer>, List<Integer>> facit;
    private QanoiDisc discCursor = null;

    public QanoiPuzzleSubView(SteppingMatrix<QanoiPin> pins, MyPair<List<Integer>, List<Integer>> facit) {
        this.pins = pins;
        this.facit = facit;
    }

    @Override
    protected String getUnderText(Model model) {
        return "";
    }

    @Override
    protected String getTitleText(Model model) {
        return "PUZZLE QANOI";
    }

    @Override
    protected void specificDrawArea(Model model) {
        drawPins(model);
        drawFacit(model);
        drawCursor(model);
    }

    private void drawPins(Model model) {
        for (int x = 0; x < pins.getColumns(); ++x) {
            Point p = matrixToScreen(x, 5);
            pins.getElementAt(x, 0).drawYourself(model.getScreenHandler(), p);
        }
    }

    private void drawFacit(Model model) {
        Point pleft = convertToScreen(1, 6);
        model.getScreenHandler().put(pleft.x, pleft.y, BG_SPRITES[1]);
        Point pmid = convertToScreen(3, 6);
        model.getScreenHandler().put(pmid.x, pmid.y, BG_SPRITES[1]);
        Point pright = convertToScreen(5, 6);
        model.getScreenHandler().put(pright.x, pright.y, BG_SPRITES[1]);
        
        Point left = matrixToScreen(0, 6);
        left.x += 1;
        left.y += 1;
        for (Integer i : facit.first) {
            Sprite spriteToUse = FACIT_SPRITES[i - 1];
            model.getScreenHandler().register(spriteToUse.getName(), left, spriteToUse);
        }
        Point right = matrixToScreen(2, 6);
        right.x += 1;
        right.y += 1;
        for (Integer i : facit.second) {
            Sprite spriteToUse = FACIT_SPRITES[i - 1];
            model.getScreenHandler().register(spriteToUse.getName(), right, spriteToUse);
        }
    }

    private Point matrixToScreen(int x, int y) {
        return convertToScreen(x*2+1, y);
    }

    private void drawCursor(Model model) {
        Point cursorPos = pins.getSelectedPoint();
        Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
        Point p = matrixToScreen(cursorPos.x, cursorPos.y);
        p.y += 9;
        if (discCursor == null) {
            model.getScreenHandler().register(cursor.getName(), p, cursor, 2);
        } else {
            p.y += 2;
            discCursor.drawYourself(model.getScreenHandler(), p, 2, 0);
        }
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (pins.handleKeyEvent(keyEvent)) {
            return true;
        }
        return super.handleKeyEvent(keyEvent, model);
    }

    public void setDiscCursor(QanoiDisc currentDisc) {
        this.discCursor = currentDisc;
    }
}
