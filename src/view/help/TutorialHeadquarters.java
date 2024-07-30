package view.help;

import view.GameView;

public class TutorialHeadquarters extends HelpDialog {
    private static final String TEXT =
            "You may end up acquiring real estate in a town or castle. The house will act as the " +
            "headquarters for your party and replaces the 'Make Camp' action in town. " +
            "Spending a night at your headquarters will still consume rations, but sleeping in real " +
            "beds makes your party members recover stamina, just like staying at a tavern or inn.\n\n" +
            "At your headquarters you can store any amount of gold, rations, ingredients and materials. " +
            "You can also house horses and characters which you don't want in your party at the moment. " +
            "However, your headquarters can only house a certain number of horses and characters and characters who stay at headquarters " +
            "will consume rations each day, so make sure you stock up. Characters who starve at headquarters " +
            "may leave permanently.\n\n" +
            "Characters who stay at headquarters keep a log of what happens there. You can read it when you visit there.\n\n" +
            "You can only have one headquarters.";

    public TutorialHeadquarters(GameView view) {
        super(view, "Headquarters", TEXT);
    }
}
