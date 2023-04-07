package view.help;

import view.GameView;

public class TutorialRecruitDialog extends HelpDialog {

    private static final String text =
            "Recruitment can be performed at Inns, Towns and Castles.\n\n" +
            "It's a great way to expand your party, but sometimes there may not " +
            "be any adventurers willing to join your party. Some races dislike others, " +
            "and most adventurers don't want to join a too small (or too big) party. " +
            "Furthermore, most are more willing to join if your party is more " +
            "experienced.\n\n" +
            "New characters never bring more than a basic weapon with them but they usually " +
            "contribute a little bit of gold to the party's communal purse.\n\n" +
            "Think carefully about what characters you draft to your party. For instance, " +
            "take into account a newcomer's skill set and how it will affect the party's alignment.";

    public TutorialRecruitDialog(GameView previous) {
        super(previous, "Recruiting", text);
    }
}
