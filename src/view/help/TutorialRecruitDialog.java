package view.help;

import view.GameView;

import java.util.List;

public class TutorialRecruitDialog extends HelpDialog {

    private static final String TEXT =
            "Recruitment can be performed at Inns, Towns and Castles.\n\n" +
            "It's a great way to expand your party, but sometimes there may not " +
            "be any adventurers willing to join your party. Some races dislike others, " +
            "and most adventurers don't want to join a too small (or too big) party. " +
            "Furthermore, most are more willing to join if your party is more " +
            "experienced.\n\n" +
            "New characters never bring more than a basic weapon with them but they sometimes " +
            "contribute a little bit of gold to the party's communal purse.\n\n" +
            "Your party can have at most eight party members, but initially your party can only consist of " +
            "up to four party members. The remaining four slots will be locked until " +
            "you upgrade your tent. Tent upgrades can be purchased at inns and taverns " +
            "and can be bought multiple times.";
    private static final String TEXT_CONTD =
            "Think carefully about what characters you draft to your party. For instance, " +
            "take into account a newcomer's skill set and how it will affect the party's alignment.\n\n" +
            "A new party member's initial attitude toward the other party members is dependent on their " +
            "respective races. The initial attitude toward the leader or of the leader is never negative.";

    public TutorialRecruitDialog(GameView previous) {
        super(previous, "Recruiting", new String[]{TEXT, TEXT_CONTD});
    }
}
