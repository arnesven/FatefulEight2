package view.subviews;

import model.Model;
import model.states.EveningState;
import model.states.dailyaction.LodgingState;
import view.BorderFrame;
import view.MyColors;

import java.util.List;

public class LodgingSummarySubView extends SubView {
    private final SubView previous;
    private List<String> lodgingBreakdown;

    public LodgingSummarySubView(Model model) {
        this.previous = model.getSubView();
        lodgingBreakdown = EveningState.lodgingBreakdown(model);
    }

    @Override
    protected void drawArea(Model model) {
        previous.drawArea(model);
        int frameStartX = X_OFFSET+4;
        int frameStartY = Y_OFFSET+10;
        int frameWidth = 21;
        int frameHeight = lodgingBreakdown.size() + 4;
        model.getScreenHandler().clearSpace(frameStartX, frameStartX+frameWidth,
                frameStartY-2, frameStartY+frameHeight);
        model.getScreenHandler().clearForeground(frameStartX-2, frameStartX+frameWidth,
                frameStartY-2, frameStartY+frameHeight);
        BorderFrame.drawFrame(model.getScreenHandler(), frameStartX, frameStartY,
                frameWidth, frameHeight, MyColors.BLACK, MyColors.WHITE, MyColors.BLUE, true);
        BorderFrame.drawCentered(model.getScreenHandler(), "Party Tab", frameStartY+1, MyColors.WHITE, MyColors.BLUE);
        for (int i = 0; i < lodgingBreakdown.size(); ++i) {
            BorderFrame.drawString(model.getScreenHandler(), lodgingBreakdown.get(i),
                    frameStartX + 2, frameStartY+3+i, MyColors.WHITE, MyColors.BLUE);
        }
    }

    @Override
    protected String getUnderText(Model model) {
        return previous.getUnderText(model);
    }

    @Override
    protected String getTitleText(Model model) {
        return previous.getTitleText(model);
    }
}
