package view.help;

import view.GameView;

public class TutorialFindResources extends HelpDialog {
    private static final String TEXT =
            "When you are not in a town or castle you can select Find Resources as your daily action. " +
            "When finding resources you can either search for ingredients or for crafting materials. " +
            "How likely it is to find ingredients or materials depends on the type of terrain you are " +
            "currently exploring.\n\n" +
            "You can spend a little time looking for resources and still have time to travel that day. If you spend " +
            "a longer time, you increase your chances of finding resources, but cannot travel that day.\n\n" +
            "Each party member performs two Skill Checks. The result of the modified rolls are added together. " +
            "This sum is used to determine the number of resources found. The skills involved in the two skill checks " +
            "are Perception and Search for finding ingredients, and Search and Labor for finding crafting materials.\n\n" +
            "Finding resources has a small chance of exhausting a characters stamina.";

    public TutorialFindResources(GameView view) {
        super(view, "Find Resources", TEXT);
    }
}
