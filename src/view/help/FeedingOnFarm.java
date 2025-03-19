package view.help;

import view.GameView;

public class FeedingOnFarm extends SubChapterHelpDialog {
    private static final String TEXT = "Vampires sometimes seek out their prey on farms. " +
            "Feeding on farms is similar to feeding in towns, but a vampire only gets one attempt per night.";

    public FeedingOnFarm(GameView view) {
        super(view, "Feeding (farm)", TEXT);
    }
}
