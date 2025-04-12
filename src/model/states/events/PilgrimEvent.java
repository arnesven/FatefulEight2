package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.items.Equipment;
import model.items.Item;
import model.items.clothing.PilgrimsCloak;
import model.items.weapons.LongStaff;
import model.states.GameState;
import model.travellers.Traveller;
import model.travellers.TravellerCompletionHook;
import util.MyPair;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class PilgrimEvent extends MeetTravellerEvent {
    public PilgrimEvent(Model model) {
        super(model, makePilgrim(), MyRandom.randInt(5, 10), ProvokedStrategy.ALWAYS_ESCAPE,
                MyRandom.randInt(15, 35), new PilgrimCompletion());
    }

    private static GameCharacter makePilgrim() {
        CharacterAppearance portrait = PortraitSubView.makeRandomPortrait(Classes.TRAVELLER);
        GameCharacter pilgrim = new GameCharacter("Pilgrim", "", portrait.getRace(), Classes.TRAVELLER, portrait,
                Classes.NO_OTHER_CLASSES, new Equipment(new LongStaff(), new PilgrimsCloak(), null));
        pilgrim.setLevel(MyRandom.randInt(1, 4));
        return pilgrim;
    }

    @Override
    protected List<Point> getPathToDestination(Model model) {
        model.getWorld().dijkstrasByLand(model.getParty().getPosition(), false);
        List<Point> path = model.getWorld().shortestPathToNearestRuins();
        return path;
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm a pilgrim. I travel the world to visit spiritual sites.";
    }

    @Override
    protected Map<String, MyPair<String, String>> makeSpecificTopics(Model model) {
        return Map.of("ruins", new MyPair<>("What's so special about those ruins?",
                "The ruins? They're spectacular. Apart from being remnants from a bygone age, " +
                        "their usually filled with treasures. But beware, the ones I've poked my nose into " +
                        "were also addled with monsters."));
    }

    private static class PilgrimCompletion extends TravellerCompletionHook {
        @Override
        public void run(Model model, GameState state, Traveller traveller) {
            traveller.travellerSay(model, state, "I can't believe I'm finally here.");
            state.leaderSay("Are you sure you want to stay here? Places like these can be dangerous.");
            traveller.travellerSay(model, state, "Of course. I'll be fine. As a special thank you, take this item.");
            Item it = model.getItemDeck().draw(1).get(0);
            state.println("The party receives a " + it.getName() + " from the Pilgrim.");
            if (it.getCost() < 16) {
                state.leaderSay("Uh... thanks.");
            } else {
                state.leaderSay("Thank you.");
            }
            it.addYourself(model.getParty().getInventory());
        }
    }
}
