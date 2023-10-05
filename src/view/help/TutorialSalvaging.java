package view.help;

import view.GameView;

public class TutorialSalvaging extends SubChapterHelpDialog {
    private static final String TEXT =
            "Weapons, clothing and accessories can be broken down at workbenches to " +
            "salvage their materials. The salvaging " +
            "character makes a Solo Labor skill check difficulty 5. If successful, a number of " +
            "materials is recovered equal to:\n\n" +
            " Item Value + Check Result - 5\n " + makeLine() + "\n              5\n\n" +
            "Regardless of whether any materials were salvaged or not the item is destroyed.";

    private static String makeLine() {
        String dump = "-----------------------------";
        StringBuilder bldr = new StringBuilder();
        for (int i = 0; i < dump.length(); ++i) {
            bldr.append((char)(0x7F));
        }
        return bldr.toString();
    }

    public TutorialSalvaging(GameView view) {
        super(view, "Salvaging", TEXT);
    }
}
