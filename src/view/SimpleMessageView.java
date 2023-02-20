package view;

import model.Model;
import util.MyStrings;
import view.party.SelectableListMenu;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class SimpleMessageView extends SelectableListMenu {
    private static final int DIALOG_MAX_WIDTH = 32;
    private final String[] texts;
    private final String buttonText;

    public SimpleMessageView(GameView previous, String text, String buttonText) {
        super(previous,  widthForText(text), heightForText(text));
        this.texts = MyStrings.partition(text, DIALOG_MAX_WIDTH-2);
        this.buttonText = buttonText;
    }

    public SimpleMessageView(GameView previous, String text) {
        this(previous, text, "OK");
    }

    private static int heightForText(String text) {
        String[] stuff = MyStrings.partition(text, DIALOG_MAX_WIDTH-2);
        return stuff.length + 4;
    }

    private static int widthForText(String text) {
        String[] stuff = MyStrings.partition(text, DIALOG_MAX_WIDTH-2);
        int max = 0;
        for (int i = 0 ; i < stuff.length; ++i) {
            if (stuff[i].length() > max) {
                max = stuff[i].length();
            }
        }
        return 2 + max;
    }

    @Override
    protected int getXStart() {
        return super.getXStart()-1;
    }

    @Override
    public void transitionedFrom(Model model) {

    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> objs = new ArrayList<>();
        for (String s : texts) {
            objs.add(new TextDecoration(s, xStart+2, ++yStart, MyColors.WHITE, MyColors.BLUE, true));
        }
        return objs;
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        List<ListContent> list = new ArrayList<>();
        list.add(new SelectableListContent(xStart+getWidth()/2-1, yStart+getHeight()-2, buttonText) {
            @Override
            public void performAction(Model model, int x, int y) {
                setTimeToTransition(true);
            }
        });
        return list;
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            setTimeToTransition(true);
        }
    }
}
