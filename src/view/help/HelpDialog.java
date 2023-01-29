package view.help;

import model.Model;
import util.MyStrings;
import view.GameView;
import view.MyColors;
import view.party.SelectableListMenu;
import view.sprites.CharSprite;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public abstract class HelpDialog extends SelectableListMenu {
    private static final int DIALOG_WIDTH = 35;
    private static final int DIALOG_HEIGHT = 20;

    private final int textWidth;
    private final String text;

    public HelpDialog(GameView previous, int width, int height, String text) {
        super(previous, width, height);
        this.textWidth = width;
        this.text = text;
    }

    public HelpDialog(GameView previous, String text) {
        this(previous, DIALOG_WIDTH, DIALOG_HEIGHT, text);
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> textContent = new ArrayList<>();
        String[] parts = MyStrings.partitionWithLineBreaks(text, textWidth);
        for (int i = 0 ; i < parts.length; ++i) {
            textContent.add(new TextDecoration(parts[i], xStart+2, yStart+1+i, MyColors.WHITE, MyColors.BLUE, true));
        }
        return textContent;
    }


    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        return List.of(
                new SelectableListContent(40 - 2, yStart + 18, "OK") {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        setTimeToTransition(true);
                    }
                });
    }


    @Override
    public void transitionedFrom(Model model) {

    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

    }
}
