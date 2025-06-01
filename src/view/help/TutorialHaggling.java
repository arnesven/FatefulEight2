package view.help;

import model.states.ShopState;
import view.GameView;

public class TutorialHaggling extends HelpDialog {
    private static final String TEXT =
            "Sometimes you can haggle with people in order to bring down prices. " +
            "You can only haggle over items which cost " + ShopState.HAGGLE_LIMIT +
            " times your average party level.\n\n" +
            "When a character haggles, he or she rolls a " +
            "Mercantile Skill Check with a difficulty of 7. This skill check receives a bonus from the character's " +
            "ranks in Persuade. If the check is successful, you will be offered a price " +
            "reduced by an amount proportional to the skill check's result.\n\n" +
            "If you fail the skill check, " +
            "or if you reject the haggled deal (or can't afford it), no more haggling will be allowed during that trading " +
            "instance.";
    public TutorialHaggling(GameView view) {
        super(view, "Haggling", TEXT);
    }
}
