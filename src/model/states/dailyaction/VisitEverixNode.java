package model.states.dailyaction;

import model.Model;
import model.characters.GameCharacter;
import model.classes.WitchClass;
import model.journal.InitialStoryPart;
import model.map.TownLocation;
import model.states.DailyEventState;
import model.states.GameState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TownSubView;

import java.awt.*;

public class VisitEverixNode extends DailyActionNode {
    private static final Sprite SPRITE = new Sprite32x32("uncleshouse", "world_foreground.png", 0x22,
            MyColors.YELLOW, TownSubView.PATH_COLOR, MyColors.BROWN, MyColors.LIGHT_BLUE);
    private final InitialStoryPart storyPart;

    public VisitEverixNode(InitialStoryPart storyPart) {
        super("Visit Everix");
        this.storyPart = storyPart;
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new VisitEverixEvent(model, storyPart);
    }

    public void drawYourself(Model model, Point p) {
        model.getScreenHandler().register(getBackgroundSprite().getName(), new Point(p), getBackgroundSprite());
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE;
    }

    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) { }

    private class VisitEverixEvent extends DailyEventState {
        private final InitialStoryPart storyPart;

        public VisitEverixEvent(Model model,InitialStoryPart storyPart) {
            super(model);
            this.storyPart = storyPart;
        }

        @Override
        protected void doEvent(Model model) {
            if (storyPart.getStep() == 3) {
                setCurrentTerrainSubview(model);
                showEverixPortrait(model);
                portraitSay("So you've taken care of the 'frogmen problem'. What can I help you with?");
                leaderSay("I found this crimson orb. It was in the stomach of the frogman chieftain. " +
                        "Do you know anything about it?");
                println("You hand Everix the orb, and she examines it closely.");
                portraitSay("It definitely has magical properties. In the stomach you say?");
                leaderSay("Yes the chieftain was filled with rage. Frogmen are usually pretty docile, do you " +
                        "think it may be connected to the orb?");
                portraitSay("Could be.");
                leaderSay("Could it be valuable?");
                portraitSay("Could be, if you found the right buyer. But I'm not really an expert in this field of study.");
                leaderSay("Oh...");
                portraitSay("But I know somebody who is.");
                leaderSay("Oh!");
                portraitSay("It's just...");
                leaderSay("What?");
                portraitSay("Well, she doesn't live in town. She's a bit of a loner.");
                leaderSay("...");
                portraitSay("She's a witch.");
                for (GameCharacter gc : model.getParty().getPartyMembers()) {
                    if (gc.getCharClass() instanceof WitchClass) {
                        partyMemberSay(gc, "There's nothing wrong with that!");
                        return;
                    }
                }
                leaderSay("I don't mind witches. Where can I find her?");
                portraitSay("I'll mark the location of her hut on your map. But you should know that she never does " +
                        "anything for free. She'll likely have a task she'll want you to do before answering any of your " +
                        "questions.");
                leaderSay("Naturally. Why be helpful?");
                portraitSay("Oh, she'll be helpful. She's just... well let's just call it self preservation.");
                model.getMainStory().increaseStep(model);
            } else if (storyPart.getStep() < 3) {
                println("Everix is not at home right now.");
            } else {
                setCurrentTerrainSubview(model);
                showEverixPortrait(model);
                portraitSay("Good luck with that crimson orb!");
            }
        }

        private void showEverixPortrait(Model model) {
            showExplicitPortrait(model, storyPart.getEverixPortrait(), "Everix");
        }
    }
}
