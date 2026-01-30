package model.mainstory;

import model.Model;
import model.quests.MindMachineQuest;
import view.subviews.WorkbenchSubView;

public class ArabellasWorkshopSuView extends WorkbenchSubView {
    private String title = "QUEST - " + MindMachineQuest.QUEST_NAME.toUpperCase();

    @Override
    protected String getUnderText(Model model) {
        return "";
    }

    @Override
    protected String getTitleText(Model model) {
        return title;
    }
}
