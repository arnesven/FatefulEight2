package view.help;

import view.GameView;

public class TutorialAttitudes extends HelpDialog {
    private static final String TEXT =
            "Party members have feelings. These feelings are represented " +
            "by a character's attitude toward another character. Here are some examples of things that can affect a " +
            "character's attitude toward another character:\n\n" +
                "*The result of Solo Skill Checks\n" +
                "*The outcome of Quests\n" +
                "*When a character starves\n" +
                "*When a character uses a potion or spell on another character\n" +
                "*Various events\n" +
            "\n" +
            "It is important to keep the party positively disposed toward the party leader. If a character starts to dislike the party " +
            "leader too much, she may consider leaving the party.\n\n" +
            "You can get a visual presentation of the party members' attitudes by talking to your party members, " +
            "when you are at a tavern or an inn.";

    public TutorialAttitudes(GameView view) {
        super(view, "Attitudes", TEXT);
    }
}
