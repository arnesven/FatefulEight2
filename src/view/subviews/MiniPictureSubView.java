package view.subviews;

import model.Model;
import view.BorderFrame;
import view.MyColors;
import view.sprites.MiniPictureSprite;

import java.awt.*;

public class MiniPictureSubView extends SubView {
    private final SubView previous;
    private final MiniPictureSprite sprite;
    private final String undertext;

    public MiniPictureSubView(SubView previous, MiniPictureSprite sprite, String undertext) {
        this.previous = previous;
        this.sprite = sprite;
        this.undertext = undertext;
    }

    @Override
    protected void drawArea(Model model) {
        previous.drawArea(model);
        model.getScreenHandler().clearForeground(X_OFFSET+5, X_OFFSET+17+PortraitSubView.PORTRAIT_FRAME_WIDTH,
                Y_OFFSET+5, Y_OFFSET+7+PortraitSubView.PORTRAIT_FRAME_HEIGHT);
        BorderFrame.drawFrame(model.getScreenHandler(), X_OFFSET+7, Y_OFFSET+7,
                PortraitSubView.PORTRAIT_FRAME_WIDTH, PortraitSubView.PORTRAIT_FRAME_HEIGHT,
                MyColors.BLACK, MyColors.GRAY, MyColors.BLACK, true);
        model.getScreenHandler().register(sprite.getName(), new Point(X_OFFSET+8, Y_OFFSET+8), sprite);
    }

    @Override
    protected String getUnderText(Model model) {
        return undertext;
    }

    @Override
    protected String getTitleText(Model model) {
        return previous.getTitleText(model);
    }
}
