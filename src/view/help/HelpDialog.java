package view.help;

import model.Model;
import util.Arithmetics;
import util.MyStrings;
import view.GameView;
import view.MyColors;
import view.party.DrawableObject;
import view.party.SelectableListMenu;
import view.sprites.ArrowSprites;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class HelpDialog extends SelectableListMenu {
    protected static final int DIALOG_WIDTH = 35;
    private static final int DIALOG_HEIGHT = 20;

    private final int textWidth;
    private final String[] texts;
    private final String title;
    private final int numberOfPages;
    private int currentPage = 0;

    public HelpDialog(GameView previous, int width, int height, String titles, String[] texts) {
        super(previous, width, height);
        this.numberOfPages = texts.length;
        this.textWidth = width-1;
        this.title = titles;
        this.texts = texts;
    }

    public HelpDialog(GameView previous, int width, int height, String title, String text) {
        this(previous, width, height, title, new String[]{text});
    }

    public HelpDialog(GameView previous, int height, String title, String text) {
        this(previous, DIALOG_WIDTH, height, title, text);
    }

    public HelpDialog(GameView previous, String title, String[] texts) {
        this(previous, DIALOG_WIDTH, getHeightForText(texts[0])+6, title, texts);
    }

    public HelpDialog(GameView previous, String title, String texts) {
        this(previous, title, new String[]{texts});
    }

    protected static int getHeightForText(String text) {
        return MyStrings.partitionWithLineBreaks(text, DIALOG_WIDTH-1).length;
    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> textContent = new ArrayList<>();
        String titleText = title.toUpperCase();
        if (numberOfPages > 1) {
            titleText += " (PAGE " + (currentPage + 1) + "/" + numberOfPages+ ")";
            model.getScreenHandler().put(xStart + 2 + titleText.length() + 1, yStart + 1, ArrowSprites.RIGHT);
        }
        textContent.add(new TextDecoration(titleText, xStart+2, yStart+1, MyColors.WHITE, MyColors.BLUE, false));
        String[] parts = MyStrings.partitionWithLineBreaks(getText(), textWidth);
        for (int i = 0 ; i < parts.length; ++i) {
            textContent.add(new TextDecoration(parts[i], xStart+2, yStart+3+i, MyColors.WHITE, MyColors.BLUE, false));
        }
        return textContent;
    }

    protected int getTextHeight(String text) {
        return MyStrings.partitionWithLineBreaks(text, textWidth).length;
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        return List.of(SelectableListMenu.makeOkButton(model, 40 - 2, yStart + getHeight() - 2, this));
    }


    @Override
    public void transitionedFrom(Model model) {

    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            goToNextPage();
            madeChanges();
        } else if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            goToPreviousPage();
            madeChanges();
        }
    }

    public String getText() { return texts[currentPage]; }

    public String getTitle() {
        return title;
    }

    public boolean isExpandable() {
        return false;
    }

    public void setExpanded(boolean expand) { }

    public boolean isExpanded() {
        return false;
    }

    public List<HelpDialog> getSubSections() {
        return new ArrayList<>();
    }

    public void goToNextPage() {
        currentPage = Arithmetics.incrementWithWrap(currentPage, numberOfPages);
    }

    public void goToPreviousPage() {
        currentPage = Arithmetics.decrementWithWrap(currentPage, numberOfPages);
    }

    protected int getCurrentPage() {
        return currentPage;
    }
}
