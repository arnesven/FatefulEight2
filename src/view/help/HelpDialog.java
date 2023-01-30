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
    private final String title;

    public HelpDialog(GameView previous, int width, int height, String title, String text) {
        super(previous, width, height);
        this.textWidth = width-1;
        this.title = title;
        this.text = text;
    }

    public HelpDialog(GameView previous, int height, String title, String text) {
        this(previous, DIALOG_WIDTH, height, title, text);
    }

    public HelpDialog(GameView previous, String title, String text) {
        this(previous, DIALOG_WIDTH, MyStrings.partitionWithLineBreaks(text, DIALOG_WIDTH-1).length+6, title, text);
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> textContent = new ArrayList<>();
        textContent.add(new TextDecoration(title.toUpperCase(), xStart+2, yStart+1, MyColors.WHITE, MyColors.BLUE, false));
        String[] parts = MyStrings.partitionWithLineBreaks(text, textWidth);
        for (int i = 0 ; i < parts.length; ++i) {
            textContent.add(new TextDecoration(parts[i], xStart+2, yStart+3+i, MyColors.WHITE, MyColors.BLUE, false));
        }
        return textContent;
    }


    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        return List.of(
                new SelectableListContent(40 - 2, yStart + getHeight() - 2, "OK") {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        setTimeToTransition(true);
                    }

                    @Override
                    public boolean isEnabled(Model model) {
                        return true;
                    }
                });
    }


    @Override
    public void transitionedFrom(Model model) {

    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

    }

    public String getTitle() {
        return title;
    }
}
