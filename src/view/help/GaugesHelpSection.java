package view.help;

import model.states.duel.gauges.*;
import util.MyLists;
import view.GameView;

import java.util.List;

public class GaugesHelpSection extends ExpandableHelpDialog {

    private static final String TEXT =
            "During magic duels, each duelist has a Power Gauge to store " +
            "the magical power generated during the duel. Such power can be " +
            "spent to launch more powerful dueling attacks, or to push a Beam Lock " +
            "beams toward the opponent.\n\n" +
            "Power Gauges come in different shapes and sizes, but all have multiple levels. " +
            "The levels indicate the strength of a special attack when launched.\n\n" +
            "Novice duelist are recommended to start with the B-Gauge. The A, C and V-Gauges " +
            "are for intermediate duelist, whereas the K, S and T-Gauges are for experts.";

    public GaugesHelpSection(GameView view) {
        super(view, "Power Gauges", TEXT, true);
    }

    @Override
    protected List<HelpDialog> makeSubSections(GameView view) {
        List<PowerGauge> gauges = List.of(
                new ATypePowerGauge(true),
                new BTypePowerGauge(true),
                new CTypePowerGauge(true),
                new KTypePowerGauge(true),
                new STypePowerGauge(true),
                new TTypePowerGauge(true),
                new VTypePowerGauge(true));
        return MyLists.transform(gauges, pg -> pg.makeHelpSection(view));
    }
}
