package view.help;

import model.classes.Skill;
import model.states.events.RitualEvent;
import view.GameView;

public class TutorialBeams extends SubChapterHelpDialog {
    private static final String TEXT =
            "The beam spell is a difficulty " + RitualEvent.CAST_BEAM_DIFFICULTY + " magic skill " +
            "of the color related to the type of ritual, and has a HP cost of 1 to cast. For the beam " +
            "to be established the receiver of the beam must cast the receiving spell, which is a difficulty " +
            RitualEvent.RECEIVE_BEAM_DIFFICULTY + " " + Skill.MagicAny.getName() + " spell, with a HP cost of 0.\n\n" +
            "Any participant who is holding onto beams will suffer 1 HP each round. Any participant who drops below " +
            RitualEvent.DROP_OUT_HP_THRESHOLD + " HP will automatically leave the ritual.\n\n" +
            "A participant in the ritual cannot hold more than two beams, and cannot cast the beam spell upon themselves.\n\n" +
            "Since beams are held by participants of the ritual, the beams will move if the participants move. " +
            "A participant can move clockwise or counter-clockwise during a ritual, swapping places with the person next to them.";

    public TutorialBeams(GameView view) {
        super(view, "Beams", TEXT);
    }
}
