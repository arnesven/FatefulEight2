package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.items.Equipment;
import model.items.Item;
import model.items.accessories.AnkhPendant;
import model.items.accessories.HolyChalice;
import model.items.accessories.Tiara;
import model.items.weapons.LongStaff;
import model.items.weapons.Scepter;
import model.states.GameState;
import model.travellers.Traveller;
import model.travellers.TravellerCompletionHook;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.List;

public class MonkEvent extends MeetTravellerEvent {
    public MonkEvent(Model model) {
        super(model, makeMonk(), MyRandom.randInt(4, 8), ProvokedStrategy.FIGHT_IF_ADVANTAGE,
                MyRandom.randInt(10, 25), new MonkCompletion());
    }

    private static GameCharacter makeMonk() {
        CharacterAppearance appearance = PortraitSubView.makeRandomPortrait(Classes.PRI);
        GameCharacter monk = new GameCharacter("Monk", "", appearance.getRace(), Classes.PRI, appearance,
                new Equipment(new LongStaff()));
        monk.setLevel(MyRandom.randInt(1, 4));
        return monk;
    }

    @Override
    protected List<Point> getPathToDestination(Model model) {
        model.getWorld().dijkstrasByLand(model.getParty().getPosition(), false);
        List<Point> path = model.getWorld().shortestPathToNearestTemple();
        return path;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm a monk. I spend most of my time at the temple, praying and doing chores.";
    }

    private static class MonkCompletion extends TravellerCompletionHook {
        @Override
        public void run(Model model, GameState state, Traveller traveller) {
            traveller.travellerSay(model, state, "I really appreciate you escorting me. " +
                    "I want to do something special for you, I want you to have this.");
            state.leaderSay("What's this?");
            traveller.travellerSay(model, state, "A relic.");
            Item it = MyRandom.sample(List.of(new AnkhPendant(), new Scepter(), new Tiara(), new HolyChalice()));
            state.println("The party receives a " + it.getName() + ".");
            if (it.getCost() > 30) {
                state.leaderSay("Wow! Thanks a lot!");
            } else {
                state.leaderSay("Uh... thanks.");
            }
            it.addYourself(model.getParty().getInventory());
        }
    }
}
