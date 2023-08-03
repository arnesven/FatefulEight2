package view.subviews;

import model.Model;
import model.quests.AncientStrongholdControlPanel;
import sprites.CombatCursorSprite;
import view.BorderFrame;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite16x16;
import view.sprites.Sprite32x32;
import view.sprites.Sprite8x8;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class ControlPanelSubView extends SubView {
    private static final Sprite PEARL_SLOT = new Sprite32x32("pearlslot", "quest.png", 0x2C,
            MyColors.DARK_GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY, MyColors.LIGHT_GRAY);
    private static final Sprite LEVER_OFF = new Sprite32x32("leveroff", "quest.png", 0x2D,
            MyColors.DARK_GRAY, MyColors.BROWN, MyColors.DARK_BROWN, MyColors.BLACK);
    private static final Sprite LEVER_ON = new Sprite32x32("leveron", "quest.png", 0x2E,
            MyColors.DARK_GRAY, MyColors.BROWN, MyColors.DARK_BROWN, MyColors.BLACK);
    private static final Sprite DIAL = new Sprite32x32("dial", "quest.png", 0x1D,
            MyColors.DARK_GRAY, MyColors.GRAY, MyColors.ORANGE, MyColors.BLACK);
    private static final Sprite CORNER = new Sprite32x32("corner", "quest.png", 0x1E,
            MyColors.DARK_GRAY, MyColors.WHITE, MyColors.PINK, MyColors.BEIGE);
    private static final Sprite DIAL_WRONG = new Sprite16x16("dialwrong", "quest.png", 0x50,
            MyColors.DARK_BROWN, MyColors.WHITE, MyColors.RED, MyColors.BEIGE);
    private static final Sprite DIAL_UR = new Sprite16x16("dialur", "quest.png", 0x52,
            MyColors.DARK_BROWN, MyColors.WHITE, MyColors.RED, MyColors.BEIGE);
    private static final Sprite DIAL_LR = new Sprite16x16("diallr", "quest.png", 0x54,
            MyColors.DARK_BROWN, MyColors.WHITE, MyColors.RED, MyColors.BEIGE);
    private static final Sprite DIAL_OFF = new Sprite16x16("dialoff", "quest.png", 0x53,
            MyColors.DARK_BROWN, MyColors.WHITE, MyColors.RED, MyColors.BEIGE);
    private static final Sprite DIAL_HALF = new Sprite16x16("dialhalf", "quest.png", 0x51,
            MyColors.DARK_BROWN, MyColors.WHITE, MyColors.RED, MyColors.BEIGE);
    private static final Sprite DIAL_FULL = new Sprite16x16("dialfull", "quest.png", 0x55,
            MyColors.DARK_BROWN, MyColors.WHITE, MyColors.RED, MyColors.BEIGE);
    private static final Map<MyColors, Sprite> pearlSprites = makePearlSprites();

    private static Map<MyColors, Sprite> makePearlSprites() {
        Map<MyColors, Sprite> result = new HashMap<>();
        for (int i = 0; i < AncientStrongholdControlPanel.PEARL_COLORS.length; i++) {
            Sprite sprite = new Sprite32x32("pearl"+i, "quest.png", 0x2C,
                    MyColors.DARK_GRAY, AncientStrongholdControlPanel.PEARL_COLORS[i],
                    AncientStrongholdControlPanel.PEARL_SHADE_COLORS[i],
                    AncientStrongholdControlPanel.PEARL_HIGHLIGHT_COLORS[i]);
            result.put(AncientStrongholdControlPanel.PEARL_COLORS[i], sprite);
        }
        return result;
    }

    private final SubView previous;
    private final AncientStrongholdControlPanel controlPanel;
    private int cursorPos = 0;

    public ControlPanelSubView(SubView subView, AncientStrongholdControlPanel controlPanel) {
        this.previous = subView;
        this.controlPanel = controlPanel;
    }

    @Override
    protected void drawArea(Model model) {
        previous.drawArea(model);
        int frameStartX = X_MAX-12;
        int frameStartY = Y_OFFSET+12;
        int frameWidth = 21;
        int frameHeight = 9;
        model.getScreenHandler().clearSpace(frameStartX-2, frameStartX+frameWidth,
                frameStartY-2, frameStartY+frameHeight);
        model.getScreenHandler().clearForeground(frameStartX-2, frameStartX+frameWidth,
                frameStartY-2, frameStartY+frameHeight);
        BorderFrame.drawFrame(model.getScreenHandler(), frameStartX, frameStartY,
                frameWidth, frameHeight, MyColors.BLACK, MyColors.GRAY, MyColors.BLACK, false);
        drawPearlSlotsAndDials(model, frameStartX, frameStartY);
        model.getScreenHandler().put(frameStartX + 1 + 4*4, frameStartY + 1,
                CORNER);
        drawLever(model, frameStartX, frameStartY);
        drawCursor(model, frameStartX, frameStartY+4);
    }

    private void drawPearlSlotsAndDials(Model model, int frameStartX, int frameStartY) {
        for (int i = 0; i < 4; ++i) {
            drawDial(model, frameStartX, frameStartY, i);
            Sprite sprite = controlPanel.getPearlSlot(i) == null ? PEARL_SLOT :
                    pearlSprites.get(controlPanel.getPearlSlot(i));
            model.getScreenHandler().put(frameStartX + 1 + i * 4, frameStartY + 5,
                    sprite);
        }
    }

    private void drawDial(Model model, int frameStartX, int frameStartY, int index) {
        model.getScreenHandler().put(frameStartX + 1 + index*4, frameStartY + 1,
                DIAL);
        int xOff = frameStartX + 1 + index * 4;
        int yOff = frameStartY + 1;
        if (controlPanel.getDialLevel(index) != 2) {
            drawDialSpriteAt(model, xOff + 2, yOff, DIAL_UR);
        }
        if (controlPanel.getDialLevel(index) < 3) {
            drawDialSpriteAt(model, xOff + 2, yOff + 2, DIAL_LR);
        }

        if (controlPanel.getDialLevel(index) == 1) {
            drawDialSpriteAt(model, xOff, yOff, DIAL_WRONG);
        } else if (controlPanel.getDialLevel(index) == 0) {
            drawDialSpriteAt(model, xOff, yOff + 2, DIAL_OFF);
        } else if (controlPanel.getDialLevel(index) == 2) {
            drawDialSpriteAt(model, xOff + 2, yOff, DIAL_HALF);
        } else {
            drawDialSpriteAt(model, xOff + 2, yOff + 2, DIAL_FULL);
        }
    }

    private void drawDialSpriteAt(Model model, int col, int row, Sprite sprite) {
        Point p = new Point(col, row);
        model.getScreenHandler().register(DIAL_WRONG.getName(), p, sprite);
    }

    private void drawLever(Model model, int frameStartX, int frameStartY) {
        Sprite sprite = controlPanel.isLeverOn() ? LEVER_ON : LEVER_OFF;
        model.getScreenHandler().put(frameStartX + 1 + 4 * 4, frameStartY + 5,
                sprite);
    }

    private void drawCursor(Model model, int xOff, int yOff) {
        Sprite cursor = CombatCursorSprite.DEFAULT_CURSOR;
        Point p = new Point(xOff + cursorPos*4 + 1, yOff-2);
        if (cursorPos == -1){
            p = new Point(xOff + 4*4 + 1, yOff-2-4);
        }
        model.getScreenHandler().register("controlpanelcursor", p, cursor, 2);
    }

    @Override
    protected String getUnderText(Model model) {
        if (cursorPos == AncientStrongholdControlPanel.NUMBER_OF_PEARL_SLOTS) {
            return "A lever - " + (controlPanel.isLeverOn() ? "ON":"OFF");
        }
        if (cursorPos == -1) {
            return "Step away from the control panel";
        }
        if (controlPanel.getPearlSlot(cursorPos) == null) {
            return "Empty slot";
        }
        return "A slot with " + controlPanel.getPearlNameForSlot(cursorPos).toLowerCase() + " pearl";
    }

    @Override
    protected String getTitleText(Model model) {
        return previous.getTitleText(model);
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            cursorPos++;
            if (cursorPos > 4) {
                cursorPos = 0;
            }
            return true;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            cursorPos--;
            if (cursorPos < 0) {
                cursorPos = 4;
            }
            return true;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
            cursorPos = -1;
            return true;
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
            cursorPos = 4;
            return true;
        }
        return super.handleKeyEvent(keyEvent, model);
    }

    public int getCursorIndex() {
        return cursorPos;
    }
}
