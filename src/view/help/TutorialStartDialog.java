package view.help;

import model.Model;
import view.BorderFrame;
import view.GameView;
import view.MyColors;
import view.party.DrawableObject;
import view.sprites.CharSprite;

import java.awt.event.KeyEvent;
import java.util.List;


public class TutorialStartDialog extends HelpDialog {

    private static final String text =
            "You've decided to leave your old life behind to become an adventurer.\n\n" +
            "You have many things to learn about adventuring, but perhaps you already know it?\n\n" +
            "Would you like to enable the tutorial?\n\n";
    private static final String EXTRA = "\n\nThere are many help pages to browse. Press F3 to enter search mode.";

    public TutorialStartDialog(GameView previous, boolean fromHelpView) {
        super(previous, "Welcome to Fateful Eight!", text + (fromHelpView ? EXTRA : ""));
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
        return super.buildDecorations(model, xStart, yStart);
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        return List.of(
                new SelectableListContent(40 - 2, yStart + 13, "YES") {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        setTimeToTransition(true);
                    }
                },
                new SelectableListContent(40-2, yStart + 14, "NO") {
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
