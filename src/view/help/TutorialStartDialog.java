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


public class TutorialStartDialog extends SelectableListMenu {

    private static final CharSprite REP_ICON_SPRITE = CharSprite.make(3, MyColors.LIGHT_GRAY, MyColors.CYAN, MyColors.BLUE);


    private static final String text = "You've decided to leave your old life behind to become an adventurer.\n\n" +
            "Your objective is to achieve a party reputation of 6 within 100 days.\n\n\n\n" +
            "You have many things to learn about adventuring, but perhaps you already know it? " +
            "Would you like to enable the tutorial?";
    private static final int DIALOG_WIDTH = 35;
    private static final int DIALOG_HEIGHT = 20;

    public TutorialStartDialog(GameView previous) {
        super(previous, DIALOG_WIDTH, DIALOG_HEIGHT);
    }

    @Override
    public void transitionedFrom(Model model) {

    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> textContent = new ArrayList<>();
        textContent.add(new TextDecoration("Welcome to Fateful Eight!", xStart+1, yStart+1, MyColors.WHITE, MyColors.BLUE, true));
        String[] parts = MyStrings.partitionWithLineBreaks(text, DIALOG_WIDTH-2);
        int i = 0;
        for ( ; i < parts.length; ++i) {
            textContent.add(new TextDecoration(parts[i], xStart+2, yStart+3+i, MyColors.WHITE, MyColors.BLUE, true));
        }
        textContent.add(new DrawableObject(xStart+10, yStart+11) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                model.getScreenHandler().put(38, y, CharSprite.make('6', MyColors.YELLOW, MyColors.BLUE, MyColors.BLUE));
                model.getScreenHandler().put(39, y, REP_ICON_SPRITE);
            }
        });
        return textContent;
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        return List.of(
                new SelectableListContent(40 - 2, yStart + 18, "YES") {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        setTimeToTransition(true);
                    }
                },
                new SelectableListContent(40-2, yStart + 19, "NO") {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        model.getTutorial().setTutorialEnabled(false);
                        setTimeToTransition(true);
                    }
                });
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

    }
}
