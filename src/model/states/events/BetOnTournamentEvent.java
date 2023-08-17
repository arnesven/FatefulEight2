package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.map.CastleLocation;
import model.states.DailyEventState;
import util.MyRandom;
import view.subviews.TournamentSubView;

import java.awt.*;
import java.util.List;

public class BetOnTournamentEvent extends TournamentEvent  {
    public BetOnTournamentEvent(Model model, CastleLocation castle) {
        super(model, castle);
    }

    @Override
    protected void doEvent(Model model) {
        leaderSay("I think we'll just watch this time. Did you say something about placing bets?");
        showOfficial();
        portraitSay("Yes, we accept wagers here. But you will have to wait until all participants have signed up. " +
                "When we have all our fighters we can give you betting odds on each one. We even update our odds during the " +
                "matches.");
        leaderSay("Interesting, we'll come back in a little while then.");
        portraitSay("Please do!");
        model.getLog().waitForAnimationToFinish();
        removePortraitSubView(model);
        println("You wander around the tournament area for a while.");
        new FoodStandsEvent(model).doEvent(model);
        setCurrentTerrainSubview(model);
        println("You walk back over to the booth. Now you can see that eight slips of paper have been put up on " +
                "a large board next to the booth.");
        showOfficial();
        portraitSay("This is the match tree. " +
                "It shows who will fight whom and what the outcome has been as the fights conclude, " +
                "Please have a look at it now.");
        waitForReturn();
        List<GameCharacter> fighters = makeFighters(model, 8);
        TournamentSubView tournamentSubView = new TournamentSubView(fighters);
        model.setSubView(tournamentSubView);
        do {
            waitForReturnSilently();
            Point pos = tournamentSubView.getCursorPosition();
            int sel = multipleOptionArrowMenu(model, pos.x+2, pos.y+4, List.of("Bet on", "Find Info", "Back"));
            switch (sel) {
                case 0:
                    print("How much would you like to bet on " + tournamentSubView.getSelectedFighter().getName() + "? ");

                    break;
                case 1:
                    tournamentSubView.setSelectedFighterKnown();
                    break;
                default:
            }

        } while (true);


    }
}
