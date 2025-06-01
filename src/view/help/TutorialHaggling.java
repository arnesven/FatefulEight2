package view.help;

import model.states.ShopState;
import view.GameView;

public class TutorialHaggling extends HelpDialog {
    private static final String TEXT =
            "Sometimes you can haggle with people in order to bring down prices. " +
            "You can only haggle over items which cost " + ShopState.HAGGLE_LIMIT +
            " times your average party level.\n\n" +
            "When a character haggles, he or she rolls a " +
            "Mercantile Skill Check with a difficulty of " + ShopState.HAGGLE_DIFFICULTY +
            ". This skill check receives a bonus from the character's " +
            "ranks in Persuade. If the check is successful, you will be offered a price " +
            "reduced by an amount proportional to the skill check's result.\n\n" +
                    makeTable() + "\n\n" +
            "If you fail the skill check, " +
            "or if you reject the haggled deal (or can't afford it), no more haggling will be allowed during that trading " +
            "instance.";

    public TutorialHaggling(GameView view) {
        super(view, "Haggling", TEXT);
    }

    private static String makeTable() {
        StringBuilder bldr = new StringBuilder("Result Discount  Result Discount\n");
        for (int result = ShopState.HAGGLE_DIFFICULTY; result <= ShopState.HAGGLE_DIFFICULTY + 10; ++result) {
            bldr.append(String.format("%4d", result)).append("  ").append(discountString(result)).append("     ");
            if (result < ShopState.HAGGLE_DIFFICULTY + 10) {
                bldr.append(String.format("%4d", result + 10)).append("  ").append(discountString(result + 10)).append("\n");
            } else {
                bldr.append(String.format(" >%2d", result + 10 - 1)).append("  ").append(discountString(result + 10)).append("\n");
            }
        }
        return bldr.toString();
    }

    private static String discountString(int result) {
        return String.format("%5d", (int)(100 * ShopState.getHaggleDiscount(result))) + "%";
    }
}
