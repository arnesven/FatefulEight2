package model.states.dailyaction.castle;

import model.Model;
import model.items.spells.TeleportSpell;
import model.map.CastleLocation;
import model.map.DiscoveredRoute;
import model.map.WorldHex;
import model.states.DailyEventState;
import model.states.GameState;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;
import model.states.events.LeagueOfMagesEvent;
import util.MyRandom;
import view.sprites.Sprite;
import view.subviews.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CourtMageNode extends DailyActionNode {

    private final CastleLocation castle;
    private boolean didTeleport;

    public CourtMageNode(CastleLocation castleLocation) {
        super("Talk to court mage");
        didTeleport = false;
        this.castle = castleLocation;
    }

    @Override
    public GameState getDailyAction(Model model, AdvancedDailyActionState state) {
        return new TeleportToOtherCastleEvent(model);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return KeepSubView.RUG;
    }


    @Override
    public boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model) {
        return true;
    }

    @Override
    public boolean returnNextState() {
        return didTeleport;
    }

    @Override
    public void setTimeOfDay(Model model, AdvancedDailyActionState state) { }

    private class TeleportToOtherCastleEvent extends GameState {
        private static final int TELEPORT_COST = 50;

        public TeleportToOtherCastleEvent(Model model) {
            super(model);
        }

        @Override
        public GameState run(Model model) {
            printQuote("Court Mage", "I am the court mage of " + castle.getName() + ". I maintain contact " +
                    "with the mages of the other courts.");
            printQuote("Court Mage", "For " + TELEPORT_COST + " gold, I can teleport your party " +
                    "to another castle.");
            if (LeagueOfMagesEvent.isMember(model)) {
                leaderSay("Actually, we're members of the League of Mages...");
                printQuote("Court Mage", "Really? Well in that case, it's free of charge.");
            } else {
                if (model.getParty().getGold() < TELEPORT_COST) {
                    leaderSay("Fascinating. I think we'll pass.");
                    return null;
                }
            }
            print("Do you want to teleport to another castle? (Y/N) ");
            if (yesNoInput()) {
                leaderSay("Yes please. That would be very convenient.");
                if (!LeagueOfMagesEvent.isMember(model)) {
                    model.getParty().addToGold(-TELEPORT_COST);
                }
                printQuote("Court Mage", "Fine. Which castle do you want me to teleport you to?");
                List<String> options = new ArrayList<>(List.of("Arkvale Castle", "Sunblaze Castle", "Castle Ardh",
                        "Bogdown Castle"));
                options.remove(castle.getName());
                int selected = multipleOptionArrowMenu(model, 24, 24, options);
                printQuote("Court Mage", "I will now teleport you to " + options.get(selected));
                print("Are you sure you want to teleport to " + options.get(selected) + "? (Y/N) ");
                if (!yesNoInput()) {
                    leaderSay("On second thought, no, I've changed my mind.");
                    printQuote("Court Mage", MyRandom.sample(List.of("What? Okay...", "It doesn't hurt or anything.",
                            "Getting cold feet?", "That's okay. It's all the same to me.")));
                    if (!LeagueOfMagesEvent.isMember(model)) {
                        printQuote("Court Mage", "Here's your money back then.");
                        model.getParty().addToGold(TELEPORT_COST);
                    }
                    return null;
                }
                leaderSay("Okay. We're ready.");
                CastleLocation destination = model.getWorld().getCastleByName(options.get(selected));

                if (model.getCurrentHex().getLocation() != null) {
                    DiscoveredRoute.uniqueAdd(model, model.getParty().getDiscoveredRoutes(),
                            model.getCurrentHex().getLocation(), destination, DiscoveredRoute.TELEPORT);
                }

                Point p = model.getWorld().getPositionForLocation(destination);
                TeleportSpell.teleportPartyToPosition(model, this, p, false);
                didTeleport = true;
            } else {
                leaderSay("Fascinating. I think we'll pass.");
            }
            return model.getCurrentHex().getDailyActionState(model);
        }
    }
}
