package view.help;

import model.items.spells.ScrollMakingSpell;
import view.GameView;

public class TutorialScrolls extends HelpDialog {
    private static final String TEXT =
            "Scrolls are one-time-use versions of their spell counterparts. " +
            "They are consumed when you use them. " +
            "A scroll can be cast in the same situations as the spell it is based on but has " +
            "the additional benefit of not costing any Health Points to cast.\n\n" +
            "Scrolls can be created from materials by use of the " +
            ScrollMakingSpell.SPELL_NAME + " spell. When doing so, the caster must also " +
            "cast the spell which he or she wants to make a scroll from, and then finally a skill " +
            "check must be passed, the difficulty dependent on which spell is being used.\n\n" +
            "Be careful when making scrolls. " +
            "The caster will suffer damage from casting both spells!";

    public TutorialScrolls(GameView view) {
        super(view, "Scrolls", TEXT);
    }
}
