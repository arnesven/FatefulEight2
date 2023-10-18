package view.subviews;

import model.Model;
import model.items.Item;
import util.MyStrings;
import view.AnalyzeDialog;
import view.BorderFrame;
import view.MyColors;
import view.party.DrawableObject;
import view.sprites.ArrowSprites;

import java.util.ArrayList;
import java.util.List;

public class UpgradeItemOverview extends SubView {
    private static final int DIALOG_WIDTH = 26;
    private final Item fromItem;
    private final Item toItem;
    private final SubView previous;
    private List<DrawableObject> analysisDrawableObjects = new ArrayList<>();

    public UpgradeItemOverview(Model model, Item fromItem, Item toItem) {
        this.previous = model.getSubView();
        this.fromItem = fromItem;
        this.toItem = toItem;
        if (toItem.isAnalyzable()) {
            AnalyzeDialog dialog = toItem.getAnalysisDialog(model);
            analysisDrawableObjects.addAll(dialog.getAnalysisDrawableObjects(model, toItem, 27, 26));
        }
    }

    @Override
    protected void drawArea(Model model) {
        previous.drawArea(model);
        int frameStartX = X_OFFSET+2;
        int frameStartY = Y_OFFSET+10;
        int dialogHeight = 15 + analysisDrawableObjects.size() / 2;
        model.getScreenHandler().clearSpace(frameStartX, frameStartX+DIALOG_WIDTH,
                frameStartY-2, frameStartY+dialogHeight);
        model.getScreenHandler().clearForeground(frameStartX-2, frameStartX+DIALOG_WIDTH,
                frameStartY-2, frameStartY+dialogHeight);
        BorderFrame.drawFrame(model.getScreenHandler(), frameStartX, frameStartY,
                DIALOG_WIDTH, dialogHeight, MyColors.BLACK, MyColors.WHITE, MyColors.BLUE, true);
        BorderFrame.drawCentered(model.getScreenHandler(), "Upgrade", frameStartY+1, MyColors.WHITE, MyColors.BLUE);

        int y = frameStartY + 3;

        int column1 = 32;
        int column2 = 43;
        fromItem.drawYourself(model.getScreenHandler(), column1, y);
        toItem.drawYourself(model.getScreenHandler(), column2, y);
        model.getScreenHandler().put(column1+4+2, y+1, ArrowSprites.RIGHT);
        model.getScreenHandler().put(column1+4+3, y+1, ArrowSprites.RIGHT);
        model.getScreenHandler().put(column1+4+4, y+1, ArrowSprites.RIGHT);

        BorderFrame.drawCentered(model.getScreenHandler(),  toItem.getName(), y+5, MyColors.WHITE, MyColors.BLUE);

        String[] parts = MyStrings.partition(toItem.getShoppingDetails().substring(2), 22);
        for (int i = 0; i < parts.length; ++i) {
            BorderFrame.drawCentered(model.getScreenHandler(), parts[i], y + 7 + i, MyColors.WHITE, MyColors.BLUE);
        }

        if (!analysisDrawableObjects.isEmpty()) {
            BorderFrame.drawCentered(model.getScreenHandler(), toItem.getAnalysisType() + ":", y+8 + parts.length,
                    MyColors.WHITE, MyColors.BLUE);
            for (DrawableObject drobj : analysisDrawableObjects) {
                drobj.drawYourself(model, drobj.position.x, drobj.position.y + parts.length);
            }
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
