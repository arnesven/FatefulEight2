package view.help;

import view.GameView;

public class TutorialUpgrading extends SubChapterHelpDialog {
    private static final String TEXT =
            "At workbenches you can upgrade items to higher tiers. Higher tier items are more " +
            "useful than ordinary ones. For instance, higher tier weapons " +
            "always do more damage than their basic counterparts.\n\n" +
            "Upgrading an item uses the same skill rolls as if you were crafting that item. " +
            "Upgrading costs 1 material per gold value of the item you are upgrading.\n" +
            "If you have enough resources " +
            "you can even upgrade items multiple times.\n\n" +
            "You cannot upgrade items which are currently equipped.";

    public TutorialUpgrading(GameView view) {
        super(view, "Upgrading", TEXT);
    }
}
