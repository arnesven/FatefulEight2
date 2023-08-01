package view.help;

import view.GameView;

public class SubChapterHelpDialog extends HelpDialog {

    private String prefix = "" + ((char)0x7D);

    public SubChapterHelpDialog(GameView previous, int height, String title, String text) {
        super(previous, height, title, text);
    }

    public SubChapterHelpDialog(GameView previous, String title, String text) {
        super(previous, title, text);
    }

    public void setLevel(int level) {
        this.prefix = "" + ((char)0x7D);
        for (int i = 0; i < level-1; ++i) {
            this.prefix = " " + this.prefix;
        }
    }

    @Override
    public String getTitle() {
        return prefix + super.getTitle();
    }
}
