package view.subviews;

import model.Model;
import view.BorderFrame;
import view.MyColors;
import view.sprites.MiniPictureSprite;

import java.awt.*;

public class MiniPictureSubView extends SubView {
    private final SubView previous;
    private final MiniPictureSprite sprite;

    public MiniPictureSubView(SubView previous, MiniPictureSprite sprite) {
        this.previous = previous;
        this.sprite = sprite;
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
        return "An abandoned town?";
    }

    @Override
    protected String getTitleText(Model model) {
        return previous.getTitleText(model);
    }
}
