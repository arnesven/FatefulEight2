package model.quests.scenes;

import model.Model;
import model.quests.ConditionSubScene;
import model.quests.QuestEdge;
import model.states.QuestState;

public abstract class YesOrNodeNode extends ConditionSubScene {
    private final String prompt;
    private final String noOutcome;
    private String yesOutcome;

    public YesOrNodeNode(int col, int row, String prompt, String yes, String no) {
        super(col, row);
        this.prompt = prompt;
        this.yesOutcome = yes;
        this.noOutcome = no;
    }

    @Override
    public String getDescription() {
        return "Make a decision.";
    }

    @Override
    public QuestEdge run(Model model, QuestState state) {
        state.print(prompt + " (Y/N) ");
        if (state.yesNoInput()) {
            state.println(yesOutcome);
            return getSuccessEdge();
        }
        state.println(noOutcome);
        return getFailEdge();
    }
}
