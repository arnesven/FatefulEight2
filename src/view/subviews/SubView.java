package view.subviews;

import model.Model;
import view.BorderFrame;
import view.DrawingArea;
import view.widget.CenterText;
import view.widget.TitleText;

import java.awt.event.KeyEvent;

public abstract class SubView {
    public static final int Y_OFFSET = 4;
    protected static final int Y_MAX = Y_OFFSET+38;
    protected static final int X_OFFSET = BorderFrame.CHARACTER_WINDOW_COLUMNS + 1;
    protected static final int X_MAX = DrawingArea.WINDOW_COLUMNS - BorderFrame.CHARACTER_WINDOW_COLUMNS - 1;

    private TitleText topCenterText = new TitleText();
    private CenterText lowCenterText;

    public SubView(int centerTextRows) {
        lowCenterText = new CenterText(centerTextRows);
    }

    public SubView() {
        this(2);
    }

    public void drawYourself(Model model) {
        drawArea(model);
        drawOverText(model);
    }

    protected abstract void drawArea(Model model);

    private void drawOverText(Model model) {
        model.getScreenHandler().clearSpace(X_OFFSET, X_MAX, Y_OFFSET-2, Y_OFFSET-1);
        topCenterText.setText(getTitleText(model));
        topCenterText.drawYourself(model);
        lowCenterText.setContents(getUnderText(model));
        lowCenterText.drawYourself(model);
    }

    protected abstract String getUnderText(Model model);

    protected abstract String getTitleText(Model model);

    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) { return false; }

    public int getCenterTextHeight() {
        return lowCenterText.getRows();
    }
}
