package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.states.DailyEventState;
import view.subviews.PortraitSubView;

public class ArcherEvent extends DailyEventState {
    private final boolean withIntro;
    private final CharacterAppearance appearance;

    public ArcherEvent(Model model, boolean withIntro, CharacterAppearance appearance) {
        super(model);
        this.withIntro = withIntro;
        this.appearance = appearance;
    }

    public ArcherEvent(Model model) {
        this(model, true, PortraitSubView.makeRandomPortrait(Classes.MAR));
    }

    @Override
    protected void doEvent(Model model) {
        showExplicitPortrait(model, appearance, "Archer");
        if (withIntro) {
            println("Out on the grounds, a skilled archer puts arrow after " +
                    "arrow right in the bullseye. He gladly gives all who will " +
                    "listen a free lesson in marksmanship.");
            println("Each party member gains 10 experience.");
            for (GameCharacter gc : model.getParty().getPartyMembers()) {
                model.getParty().giveXP(model, gc, 10);
            }
        }
        print("The archer offers to train you in the ways of being a Marksman, ");
        ChangeClassEvent change = new ChangeClassEvent(model, Classes.MAR);
        change.areYouInterested(model);
    }
}
