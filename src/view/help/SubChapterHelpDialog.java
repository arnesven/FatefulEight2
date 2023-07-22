package view.help;

import view.GameView;

public class SubChapterHelpDialog extends HelpDialog {

    public SubChapterHelpDialog(GameView previous, int height, String title, String text) {
        super(previous, height, title, text);
    }

    public SubChapterHelpDialog(GameView previous, String title, String text) {
        super(previous, title, text);
    }

    @Override
    public String getTitle() {
        return ((char)0x7D) + super.getTitle();
    }
}
