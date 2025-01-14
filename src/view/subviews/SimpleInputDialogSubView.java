package view.subviews;

import model.Model;
import view.BorderFrame;
import view.MyColors;
import view.widget.InputBufferWidget;

import java.awt.event.KeyEvent;

public class SimpleInputDialogSubView extends SubView {
    private final SubView previous;
    private final String prompt;
    private final int yStart;
    private final InputBufferWidget buffer;

    public SimpleInputDialogSubView(SubView previous, int yStart, String prompt) {
        this.previous = previous;
        this.yStart = yStart;
        this.prompt = prompt;
        buffer = new InputBufferWidget(20);
    }

    @Override
    protected void drawArea(Model model) {
        previous.drawYourself(model);
        int width = 24;
        int xStart = X_OFFSET + ((X_MAX - X_OFFSET) - width) / 2 - 1;
        BorderFrame.drawFrame(model.getScreenHandler(), xStart, yStart, width, 5,
                MyColors.BLACK, MyColors.WHITE, MyColors.BLUE, true);
        BorderFrame.drawString(model.getScreenHandler(), prompt, xStart + 2, yStart + 1,
                MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), buffer.getRawText(), xStart + 2, yStart + 3,
                MyColors.LIGHT_YELLOW, MyColors.BLACK);
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        return buffer.enterKeyStroke(keyEvent);
    }

    @Override
    protected String getUnderText(Model model) {
        return previous.getUnderText(model);
    }

    @Override
    protected String getTitleText(Model model) {
        return previous.getTitleText(model);
    }

    public String getInput() {
        return buffer.getText();
    }
}
