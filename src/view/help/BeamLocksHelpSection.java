package view.help;

import view.GameView;

public class BeamLocksHelpSection extends SubChapterHelpDialog {
    private static final String TEXT = "When two magic duelist attack each other with " +
            "dueling attacks at the same time, their beams get caught in a beam lock. Beam locks have their own power level " +
            "which depend on how much magical power the duelist have poured into their attacks.\n\n" +
            "Each round the duelist " +
            "maintain a beam lock, power will be drained from their respective Gauges. When in a beam lock, the duelist " +
            "cannot cast their normal spells. Instead they must choose to either hold on (i.e. do nothing but maintain the lock), " +
            "Increase the power of the beam lock, which may push it closer to the opponent. Or, attempt to release the lock.\n\n" +
            "beam locks end when either duelist does does not have enough Power to sustain the lock, the beam lock gets pushed all the way " +
            "to one of the duelists, or when the lock is successfully released. A failed release attempt can also end the beam lock, but " +
            "will also inflict a hit on the duelist who tried to release the beam lock.";

    public BeamLocksHelpSection(GameView view) {
        super(view, "Beam Locks", TEXT);
    }
}
