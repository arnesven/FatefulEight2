package view.subviews;

import model.Model;
import util.MyStrings;
import view.BorderFrame;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.AdvancedCalloutSprite;
import view.sprites.Sprite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.awt.*;

public class EventCardSubView extends SubView {
    private static final MyColors TEXT_COLOR = MyColors.BLACK;
    private static final MyColors CARD_COLOR = MyColors.BEIGE;
    private static final int MAX_WIDTH = 26;
    private final SubView previous;
    private List<String> content = null;
    private final StringBuilder template;
    private static final Sprite[][] borderSprites = makeBorderSprites();

    public EventCardSubView(SubView previous, String title, String cardText) {
        super(previous.getCenterTextHeight());
        this.previous = previous;
        setTitleAndContents(title, cardText);
        this.template = new StringBuilder();
        template.repeat(" ", MAX_WIDTH);
    }

    public void setTitleAndContents(String title, String cardText) {
        this.content = new ArrayList<>();
        if (title != null) {
            content.add(title);
            StringBuilder bldr = new StringBuilder();
            bldr.repeat("_", title.length());
            content.add(bldr.toString());
        }
        for (String part : MyStrings.partition(cardText, MAX_WIDTH+1)) {
            content.add(part.trim());
        }
    }

    @Override
    protected void drawArea(Model model) {
        previous.drawArea(model);
        int height = content.size();
        int yStart = Y_MAX - height - 2;
        int xStart = X_OFFSET + (X_MAX - X_OFFSET - MAX_WIDTH)/2;

        // Draw borders
        drawBorder(model.getScreenHandler(), xStart - 1, yStart - 1, 0, 0);
        drawBorder(model.getScreenHandler(), xStart - 1, yStart + height, 0, 2);
        for (int x = 0; x < MAX_WIDTH; ++x) {
            drawBorder(model.getScreenHandler(), xStart + x, yStart - 1, 1, 0);
            drawBorder(model.getScreenHandler(), xStart + x, yStart + height, 1, 1);
        }
        drawBorder(model.getScreenHandler(), xStart + MAX_WIDTH, yStart - 1, 3, 0);
        drawBorder(model.getScreenHandler(), xStart + MAX_WIDTH, yStart + height, 3, 2);

        // Draw content
        for (int y = 0; y < content.size(); ++y) {
            drawBorder(model.getScreenHandler(), xStart - 1, yStart + y, 0, 1);
            drawBorder(model.getScreenHandler(), xStart + MAX_WIDTH, yStart + y, 3, 1);
            BorderFrame.drawCentered(model.getScreenHandler(), template.toString(), yStart+y, TEXT_COLOR, CARD_COLOR);
            BorderFrame.drawCentered(model.getScreenHandler(), content.get(y), yStart+y, TEXT_COLOR, CARD_COLOR);
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

    private void drawBorder(ScreenHandler screenHandler, int x, int y, int col, int row) {
        screenHandler.register("", new Point(x, y), borderSprites[col][row], 1);
    }

    private static Sprite[][] makeBorderSprites() {
        Sprite[][] borderSprites = AdvancedCalloutSprite.makeBorderSprites();
        for (int j = 0; j < borderSprites.length; ++j) {
            for (Sprite sp : borderSprites[j]) {
                sp.setColor2(CARD_COLOR);
            }
        }
        return borderSprites;
    }
}
