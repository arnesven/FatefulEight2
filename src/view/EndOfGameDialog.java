package view;

import model.GameScore;
import model.Model;
import model.states.GameState;
import util.MyStrings;
import view.party.SelectableListMenu;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EndOfGameDialog extends SelectableListMenu {

    private static final int DIALOG_WIDTH = 35;
    private static final int DIALOG_HEIGHT = 40;
    private String title = "";
    private String text = "";
    private static final String TIME_TEXT =
            "You have been an adventurer for a long time. You have seen many " +
            "fantastic things but unfortunately you have not achieved the fame " +
            "and glory to which you once aspired. This saddens you a bit but " +
            "at least you had fun and made some friends along the way.";
    private static final String REPUTATION_TEXT =
            "You have achieved marvelous things. You have explored the world. " +
            "You have defeated mighty foes. You have done many good deeds " +
            "(and perhaps some bad ones too). You are now one of the most " +
            "famous adventurers in all the land.";
    private static final String CHOICE_TEXT =
            "You must now make a decision. Either you stop here, lay down your load, " +
            "and retire from adventuring. Choosing this will record your score into " +
            "the Hall of Fame and then end the game. Or, you may continue the adventure " +
            "and play on indefinitely, but your score will not be recorded in this case.";

    public EndOfGameDialog(GameView previous) {
        super(previous, DIALOG_WIDTH, DIALOG_HEIGHT);
    }

    @Override
    public void transitionedTo(Model model) {
        super.transitionedTo(model);
        if (model.getParty().getReputation() == 6) {
            title = "congratulations!";
            text = REPUTATION_TEXT;
        } else {
            title = "the end of your adventures";
            text = TIME_TEXT;
        }
    }

    @Override
    public void transitionedFrom(Model model) {

    }

    @Override
    protected List<DrawableObject> buildDecorations(Model model, int xStart, int yStart) {
        List<DrawableObject> textContent = new ArrayList<>();
        textContent.add(new TextDecoration(title.toUpperCase(), xStart+2, yStart+1, MyColors.WHITE, MyColors.BLUE, true));
        int numRows = partitionAndAdd(textContent, text, xStart, yStart+3);

        numRows += 2;
        textContent.add(new TextDecoration("- score - ", xStart+2, yStart+numRows+2, MyColors.WHITE, MyColors.BLUE, true));
        GameScore score = GameScore.calculate(model);
        for (Map.Entry<String, Integer> entry : score.entrySet()) {
            numRows++;
            textContent.add(new TextDecoration(String.format("%-26s%5d", entry.getKey(), entry.getValue()),
                    xStart+2, yStart+numRows+2, MyColors.WHITE, MyColors.BLUE, false));
        }
        numRows++;
        textContent.add(new TextDecoration(String.format("TOTAL:%25d", score.getTotal()),
                xStart+2, yStart+numRows+2, MyColors.WHITE, MyColors.BLUE, false));

        numRows += 2;
        partitionAndAdd(textContent, CHOICE_TEXT, xStart, yStart+3+numRows);
        return textContent;
    }

    private int partitionAndAdd(List<DrawableObject> textContent, String text, int xStart, int y) {
        String[] parts = MyStrings.partitionWithLineBreaks(text, DIALOG_WIDTH-1);
        int i = 0;
        for (; i < parts.length; ++i) {
            textContent.add(new TextDecoration(parts[i], xStart+2, y+i, MyColors.WHITE, MyColors.BLUE, true));
        }
        return i;
    }

    @Override
    protected List<ListContent> buildContent(Model model, int xStart, int yStart) {
        return List.of(
                new SelectableListContent(40 - 3, yStart + getHeight() - 3, "RETIRE") {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        setTimeToTransition(true);
                        model.recordInHallOfFame();
                    }

                    @Override
                    public boolean isEnabled(Model model) {
                        return true;
                    }
                },
                new SelectableListContent(40 - 4, yStart + getHeight() - 2, "CONTINUE") {
                    @Override
                    public void performAction(Model model, int x, int y) {
                        model.setFreePlay(true);
                        setTimeToTransition(true);
                    }

                    @Override
                    public boolean isEnabled(Model model) {
                        return true;
                    }
                });
    }

    @Override
    protected void specificHandleEvent(KeyEvent keyEvent, Model model) {

    }
}
