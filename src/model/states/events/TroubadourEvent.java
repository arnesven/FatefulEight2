package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.items.Equipment;
import model.items.weapons.LongStaff;
import model.items.weapons.Lute;
import model.states.DailyEventState;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.List;

public class TroubadourEvent extends MeetTravellerEvent {
    public TroubadourEvent(Model model) {
        super(model, makeTroubadour(), MyRandom.randInt(10, 40), ProvokedStrategy.FIGHT_IF_ADVANTAGE, 15);
    }

    private static GameCharacter makeTroubadour() {
        CharacterAppearance portrait = PortraitSubView.makeRandomPortrait(Classes.BRD);
        return new GameCharacter("Troubadour", "", portrait.getRace(), Classes.BRD, portrait,
                Classes.NO_OTHER_CLASSES, new Equipment(new Lute()));
    }

    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        portraitSay("I've got a gig at an inn, but uh, I'm a little weary of travelling along. Will you escort me?");
        return super.doMainEventAndShowDarkDeeds(model);
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm a troubadour. I sing and entertain for payment, but I like to look at " +
                "it as just being myself, and occasionally I get generous donations.";
    }

    @Override
    protected List<Point> getPathToDestination(Model model) {
        model.getWorld().dijkstrasByLand(model.getParty().getPosition(), true);
        return model.getWorld().shortestPathToNearestInn();
    }
}
