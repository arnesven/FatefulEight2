package view.help;

import view.GameView;

import java.util.List;

public class DragonsHelpChapter extends ExpandableHelpDialog {
    public static final String TEXT =
            "Dragons occur naturally in many locations and come in different varieties.\n\n" +
            "Green Dragons are the least hostile but are still dangerous considering their attacks are " +
            "powerful and do magical damage.\n\n" +
            "Red Dragons are quick and aggressive. They can attack many targets at once with " +
            "ranged magical attacks.\n\n" +
            "Ice Dragons are slow but have powerful ranged magical attacks\n\n" +
            "Black Dragons are less vicious than others, but are slightly tougher in nature.\n\n" +
            "Bone Dragons have fragile constitutions, but can strike opponents with deadly attacks\n\n" +
            "Elder Dragons have hard armor and can knock down their opponents with forceful blows.";

    public DragonsHelpChapter(GameView view) {
        super(view, "Dragons", TEXT, false);
    }

    @Override
    protected List<HelpDialog> makeSubSections(GameView view) {
        return List.of(new TutorialDragonTaming(null));
    }
}
