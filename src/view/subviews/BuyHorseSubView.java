package view.subviews;

import model.Model;
import model.horses.Horse;
import model.states.dailyaction.BuyHorseState;
import util.MyPair;
import util.MyStrings;
import view.BorderFrame;
import view.MyColors;
import view.ScreenHandler;

import java.awt.*;
import java.awt.event.KeyEvent;

public class BuyHorseSubView extends SubView {
    private final SubView previous;
    private final int specialPrice;
    private boolean accepted = false;

    public BuyHorseSubView(SubView previous, int specialPrice) {
        this.previous = previous;
        this.specialPrice = specialPrice;
    }

    public BuyHorseSubView(SubView previous) {
        this(previous, -1);
    }

    @Override
    protected void drawArea(Model model) {
        previous.drawArea(model);
        int frameStartX = X_OFFSET;
        int frameStartY = Y_OFFSET+10;
        int frameWidth = 31;
        int frameHeight = 15;
        model.getScreenHandler().clearSpace(frameStartX, frameStartX+frameWidth,
                frameStartY-2, frameStartY+frameHeight);
        model.getScreenHandler().clearForeground(frameStartX, frameStartX+frameWidth,
                frameStartY, frameStartY+frameHeight);
        BorderFrame.drawFrame(model.getScreenHandler(), frameStartX, frameStartY,
                frameWidth, frameHeight, MyColors.BLACK, MyColors.WHITE, MyColors.BLUE, true);

        Point horsePos = new Point(frameStartX+1, frameStartY+3);
        model.getScreenHandler().clearSpace(horsePos.x, horsePos.x+8, horsePos.y, horsePos.y+8);
        model.getScreenHandler().put(horsePos.x, horsePos.y, Horse.getBackgroundSprite());
        Horse horse = model.getParty().getHorseHandler().getAvailableHorse(model);
        model.getScreenHandler().register(horse.getSprite().getName(), horsePos, horse.getSprite());
        BorderFrame.drawCentered(model.getScreenHandler(), "Buy Horse", frameStartY+1, MyColors.WHITE, MyColors.BLUE);

        int textRow = horsePos.y;
        BorderFrame.drawString(model.getScreenHandler(), "Type: " + horse.getType(),
                frameStartX + 10, textRow++, MyColors.WHITE, MyColors.BLUE);
        BorderFrame.drawString(model.getScreenHandler(), "Cost: " + (specialPrice!=-1 ? specialPrice : horse.getCost()),
                frameStartX + 10, textRow++, MyColors.WHITE, MyColors.BLUE);
        textRow++;
        String[] textParts = MyStrings.partition(horse.getInfo(), 22);
        for (int i = 0; i < textParts.length; ++i) {
            BorderFrame.drawString(model.getScreenHandler(), textParts[i],
                    frameStartX + 10, textRow++, MyColors.WHITE, MyColors.BLUE);
        }

        MyPair<MyColors, MyColors> normal = new MyPair<>(MyColors.YELLOW, MyColors.BLUE);
        MyPair<MyColors, MyColors> highlight = new MyPair<>(MyColors.BLACK, MyColors.LIGHT_YELLOW);
        drawButton(model.getScreenHandler(), "REJECT", frameStartY+frameHeight-2, accepted?normal:highlight);
        drawButton(model.getScreenHandler(), "ACCEPT", frameStartY+frameHeight-3, accepted?highlight:normal);
    }

    private void drawButton(ScreenHandler screenHandler, String text, int i, MyPair<MyColors, MyColors> colors) {
        BorderFrame.drawCentered(screenHandler, text, i, colors.first, colors.second);
    }

    @Override
    protected String getUnderText(Model model) {
        return previous.getUnderText(model);
    }

    @Override
    protected String getTitleText(Model model) {
        return previous.getTitleText(model);
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_UP || keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
            accepted = !accepted;
            return true;
        }
        return super.handleKeyEvent(keyEvent, model);
    }

    public boolean didAccept() {
        return accepted;
    }
}
