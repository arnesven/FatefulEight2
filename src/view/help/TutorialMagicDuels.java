package view.help;

import view.GameView;

import java.util.List;

public class TutorialMagicDuels extends ExpandableHelpDialog {
    private static final String TEXT =
            "Sometimes magic users spar in one-on-one standoffs. " +
            "Magic duels are non-lethal and are used to settle disputes, " +
            "to train or for purely academic purposes.\n\n" +
            "Magic duels progress over many rounds. At the start of each round the " +
            "duelists first generate some Power into their Power Gauges, " +
            "then each duelist attempts to cast one of the special Dueling Spells, " +
            "Attack, Shield or Absorb. Special attacks can be launched once a duelist has " +
            "generated enough power. Attacks which are not shielded against, or absorbed, " +
            "will inflict hits on the opponent. Once a duelist has taken five or more hits, " +
            "the duel is over and his or her opponent is declared the winner.\n\n" +
            "Duelists are free to use which every type of magic the want for the dueling spells, but some types " +
            "enjoy an advantage over others. Likewise, a duelists chooses which type of Power Gauge " +
            "he or she wishes to use before entering into the dueling ring.\n\n" +
            "Even though magic duels are non-lethal, participating in them can be exhausting for characters.";

    public TutorialMagicDuels(GameView view) {
        super(view, "Magic Duels", TEXT, false);
    }

    @Override
    protected List<HelpDialog> makeSubSections(GameView view) {
        return List.of(
                new AbsorbingHelpSection(view),
                new BeamLocksHelpSection(view),
                new ColorAdvantageHelpSection(view),
                new DuelingAttacksHelpSection(view),
                new GaugesHelpSection(view),
                new ShieldingHelpSection(view));
    }
}
