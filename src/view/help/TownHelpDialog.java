package view.help;

import view.GameView;

public class TownHelpDialog extends SubChapterHelpDialog {
    private static final String TEXT =
            "Towns are good places to find quests, jobs, equipment and people to recruit to your party. " +
            "Be careful though, not everybody in towns are friendly. You may encounter some shady individuals, " +
            "or perhaps you yourself will be interfered with by local law enforcement.\n\n" +
            "Coastal towns often offer trips by sea to other towns, for a small fee. In some towns, there may even " +
            "be ships which can be chartered, so you can steer them anywhere you like.\n\n" +
            "All towns have a communal work bench where you can tinker away to your hearts desire.";

    public TownHelpDialog(GameView view) {
        super(view, "Towns", TEXT);
    }
}
