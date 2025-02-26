package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.items.Equipment;
import model.items.accessories.Buckler;
import model.items.accessories.Spyglass;
import model.items.clothing.LeatherArmor;
import model.items.weapons.Longsword;
import model.states.GameState;
import model.travellers.Traveller;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.List;

public class LostExplorerEvent extends MeetTravellerEvent {
    public LostExplorerEvent(Model model) {
        super(model, makeLostExplorer(), MyRandom.randInt(20, 100), ProvokedStrategy.FIGHT_IF_ADVANTAGE, 5);
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        GameCharacter traveller = getVictimCharacter(model);
        println("You meet a lost explorer.");
        showExplicitPortrait(model, traveller.getAppearance(), traveller.getName());
        return true;
    }

    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        portraitSay("I'm afraid I've lost my way out here.");
        return super.doMainEventAndShowDarkDeeds(model);
    }

    @Override
    protected String getVictimSelfTalk() {
        return "I'm an explorer gathering information for map makers.";
    }

    private static GameCharacter makeLostExplorer() {
        CharacterAppearance appearance = PortraitSubView.makeRandomPortrait(Classes.TRAVELLER);
        GameCharacter explorer = new GameCharacter("Explorer", "", appearance.getRace(), Classes.TRAVELLER, appearance,
                Classes.NO_OTHER_CLASSES, new Equipment(new Longsword(), new LeatherArmor(), new Buckler()));
        explorer.setLevel(MyRandom.randInt(3, 4));
        return explorer;
    }

    @Override
    protected void doUponCompletion(Model model, GameState state, Traveller traveller) {
        traveller.travellerSay(model, state, "Actually, I think I'll be taking a break from exploring for a while, perhaps " +
                "permanently. You can have this Spyglass, it's handy when you are in places with a good view.");
        state.leaderSay("Thanks!");
        Spyglass spy = new Spyglass();
        state.println("The party received a " + spy.getName() + ".");
        spy.addYourself(model.getParty().getInventory());
    }

    @Override
    protected List<Point> getPathToDestination(Model model) {
        model.getWorld().dijkstrasByLand(model.getParty().getPosition());
        return model.getWorld().shortestPathToNearestTownOrCastle(0);
    }
}
