package model.states.feeding;

import model.Model;
import model.quests.QuestEdge;
import model.states.GameState;
import util.MyStrings;
import view.subviews.ArrowMenuSubView;

import java.util.List;

class ChooseToEnterJunction extends FeedingJunction {
    private final int windowsOpen;
    private final boolean canDoBat;

    public ChooseToEnterJunction(int col, int row, int windowOpen, boolean canDoBat, List<QuestEdge> questEdges) {
        super(col, row, questEdges);
        this.windowsOpen = windowOpen;
        this.canDoBat = canDoBat;
    }

    @Override
    protected QuestEdge specificDoAction(Model model, GameState state) {
        if (windowsOpen > 0 || canDoBat) {
            if (canDoBat) {
                state.println("You could easily fly into the house as a bat. How would you like to enter the house? ");
            } else {
                state.print("There is a window open on the " + MyStrings.nthWord(windowsOpen) + " floor. How would you like to enter the house?");
            }
            int[] selectedAction = new int[1];
            model.setSubView(new ArrowMenuSubView(model.getSubView(),
                    List.of("Through front door", (canDoBat ? "As bat" : "Through window"), "Not at all"), 32, 32, ArrowMenuSubView.NORTH_WEST) {
                @Override
                protected void enterPressed(Model model, int cursorPos) {
                    selectedAction[0] = cursorPos;
                    model.setSubView(getPrevious());
                }
            });
            state.waitForReturn();
            if (selectedAction[0] == 0) {
                return getConnection(0);
            }
            if (selectedAction[0] == 1) {
                return getConnection(2);
            }
            return getConnection(1);
        } else {
            state.print("Do you want to try to enter this house? (Y/N) ");
            if (state.yesNoInput()) {
                return getConnection(0);
            }
            return getConnection(1);
        }
    }
}
