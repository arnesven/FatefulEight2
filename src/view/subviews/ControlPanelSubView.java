package view.subviews;

import model.Model;
import util.MyRandom;
import view.BorderFrame;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class ControlPanelSubView extends SubView {
    private static final Sprite PEARL_SLOT = new Sprite32x32("pearlslot", "quest.png", 0x2C,
            MyColors.DARK_GRAY, MyColors.GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY);
    private static final Sprite LEVER = new Sprite32x32("lever", "quest.png", 0x2D,
            MyColors.DARK_GRAY, MyColors.BROWN, MyColors.DARK_BROWN, MyColors.BLACK);
    private static final Sprite DIAL = new Sprite32x32("dial", "quest.png", 0x1D,
            MyColors.DARK_GRAY, MyColors.LIGHT_PINK, MyColors.ORANGE, MyColors.BLACK);;
    private static final Sprite CORNER = new Sprite32x32("corner", "quest.png", 0x1E,
            MyColors.DARK_GRAY, MyColors.WHITE, MyColors.PINK, MyColors.BEIGE);
    private final SubView previous;

    public ControlPanelSubView(SubView subView) {
        this.previous = subView;
    }

    @Override
    protected void drawArea(Model model) {
        previous.drawArea(model);
        int frameStartX = X_OFFSET+5;
        int frameStartY = Y_OFFSET+12;
        int frameWidth = 21;
        int frameHeight = 9;
        model.getScreenHandler().clearSpace(frameStartX-2, frameStartX+frameWidth,
                frameStartY-2, frameStartY+frameHeight);
        model.getScreenHandler().clearForeground(frameStartX-2, frameStartX+frameWidth,
                frameStartY-2, frameStartY+frameHeight);
        BorderFrame.drawFrame(model.getScreenHandler(), frameStartX, frameStartY,
                frameWidth, frameHeight, MyColors.BLACK, MyColors.GRAY, MyColors.BLACK, false);
        for (int i = 0; i < 4; ++i) {
            model.getScreenHandler().put(frameStartX + 1 + i*4, frameStartY + 1,
                    DIAL);
            model.getScreenHandler().put(frameStartX + 1 + i*4, frameStartY + 5,
                    PEARL_SLOT);
        }
        model.getScreenHandler().put(frameStartX + 1 + 4*4, frameStartY + 1,
                CORNER);
        model.getScreenHandler().put(frameStartX + 1 + 4*4, frameStartY + 5,
                LEVER);
    }

    @Override
    protected String getUnderText(Model model) {
        return "Something something...";
    }

    @Override
    protected String getTitleText(Model model) {
        return previous.getTitleText(model);
    }
}
