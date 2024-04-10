package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.items.Equipment;
import model.items.accessories.Buckler;
import model.items.clothing.LeatherArmor;
import model.items.weapons.Longsword;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.List;

public class LostExplorerEvent extends MeetTravellerEvent {
    public LostExplorerEvent(Model model) {
        super(model, makeLostExplorer(), MyRandom.randInt(20, 100), ProvokedStrategy.FIGHT_IF_ADVANTAGE, 10);
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

    private static GameCharacter makeLostExplorer() {
        CharacterAppearance appearance = PortraitSubView.makeRandomPortrait(Classes.TRAVELLER);
        GameCharacter explorer = new GameCharacter("Explorer", "", appearance.getRace(), Classes.TRAVELLER, appearance,
                Classes.NO_OTHER_CLASSES, new Equipment(new Longsword(), new LeatherArmor(), new Buckler()));
        explorer.setLevel(MyRandom.randInt(3, 4));
        return explorer;
    }

    @Override
    protected List<Point> getPathToDestination(Model model) {
        model.getWorld().dijkstrasByLand(model.getParty().getPosition());
        return model.getWorld().shortestPathToNearestTownOrCastle(0);
    }
}
