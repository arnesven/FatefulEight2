package view.help;

import model.states.duel.MagicDuelEvent;
import util.MyLists;
import util.MyStrings;
import view.GameView;
import view.MyColors;

import java.util.ArrayList;
import java.util.List;

public class ColorAdvantageHelpSection extends SubChapterHelpDialog {
    private static final String TEXT_INTRO =
            "Some color of magic have a slight advantage in magical duels, when facing a particular other magic color. " +
            "When using a magic color with advantage, all shielding, absorbing and beam lock release attempts have their " +
            "skill test difficulties reduced by one.\n\n";

    private static final List<MyColors> MAGIC_COLORS = List.of(MyColors.RED, MyColors.BLUE, MyColors.GREEN, MyColors.BLACK, MyColors.WHITE);

    public ColorAdvantageHelpSection(GameView view) {
        super(view, "Color Advantage", makeText());
    }

    private static String makeText() {
        StringBuilder bldr = new StringBuilder();
        bldr.append(TEXT_INTRO);

        for (MyColors color1 : MAGIC_COLORS) {
            List<MyColors> advantageOver = new ArrayList<>();
            for (MyColors color2 : MAGIC_COLORS) {
                if (MagicDuelEvent.hasAdvantageOver(color1, color2)) {
                    advantageOver.add(color2);
                }
            }
            if (!advantageOver.isEmpty()) {
                bldr.append(MyStrings.capitalize(color1.name().toLowerCase())).append(" magic has advantage over ");
                bldr.append(MyLists.commaAndJoin(advantageOver, col -> MyStrings.capitalize(col.name().toLowerCase()))).append(".\n\n");
            } else {

                bldr.append(MyStrings.capitalize(color1.name().toLowerCase())).append(" magic does not have advantage over any magic type.");
            }
        }

        return bldr.toString();
    }
}
