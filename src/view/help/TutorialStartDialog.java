package view.help;

import model.Model;
import view.BorderFrame;
import view.GameView;
import view.MyColors;
import view.sprites.CharSprite;

import java.awt.event.KeyEvent;
import java.util.List;


public class TutorialStartDialog extends HelpDialog {

    private static final CharSprite REP_ICON_SPRITE = CharSprite.make(3, MyColors.LIGHT_GRAY, MyColors.CYAN, MyColors.BLUE);

    private static final String text =
            "You've decided to leave your old life behind to become an adventurer.\n\n" +
            "Your objective is to achieve a Party Reputation of " + Model.REP_TO_WIN + " within 100 days.\n\n\n\n" +
            "You have many things to learn about adventuring, but perhaps you already know it? " +
            "Would you like to enable the tutorial?";

    public TutorialStartDialog(GameView previous) {
        super(previous, "Welcome to Fateful Eight!", text);
    }

    @Override
    public String getTitle() {
        return "Objective";
    }

    @Override
    public void transitionedFrom(Model model) {

    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> textContent = super.buildDecorations(model, xStart, yStart);
        textContent.add(new DrawableObject(xStart+16, yStart+11) {
            @Override
            public void drawYourself(Model model, int x, int y) {
                BorderFrame.drawString(model.getScreenHandler(),  "" + Model.REP_TO_WIN, x-1, y, MyColors.YELLOW, MyColors.BLUE);
                model.getScreenHandler().put(x+1, y, REP_ICON_SPRITE);
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
