package model.quests.scenes;

import model.Model;
import model.quests.ConditionSubScene;
import model.quests.CountingQuest;
import model.quests.QuestEdge;
import model.states.QuestState;
import util.MyStrings;

public abstract class CountCheckSubScene extends ConditionSubScene {
    private final String descrString;
    private final int minimum;
    private final CountingQuest countQuest;

    public CountCheckSubScene(CountingQuest countQuest, int col, int row, int minimum, int maximum) {
        super(col, row);
        this.countQuest = countQuest;
        this.minimum = minimum;
        descrString = MyStrings.capitalize(MyStrings.numberWord(minimum)) + " out of " +
                MyStrings.numberWord(maximum) + " * successful?";
    }

    @Override
    public String getDescription() {
        return descrString;
    }

    @Override
    public QuestEdge run(Model model, QuestState state) {
        if (countQuest.getCount() >= minimum) {
            state.println(getSuccessText());
            return getSuccessEdge();
        }
        state.println(getFailText());
        return getFailEdge();
    }

    protected abstract String getFailText();

    protected abstract String getSuccessText();
}
