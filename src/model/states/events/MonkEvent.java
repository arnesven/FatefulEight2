package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.items.Equipment;
import model.items.weapons.LongStaff;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.List;

public class MonkEvent extends MeetTravellerEvent {
    public MonkEvent(Model model) {
        super(model, makeMonk(), MyRandom.randInt(4, 8), ProvokedStrategy.FIGHT_IF_ADVANTAGE,
                MyRandom.randInt(10, 25));
    }

    private static GameCharacter makeMonk() {
        CharacterAppearance appearance = PortraitSubView.makeRandomPortrait(Classes.PRI);
        GameCharacter monk = new GameCharacter("Monk", "", appearance.getRace(), Classes.PRI, appearance,
                Classes.NO_OTHER_CLASSES, new Equipment(new LongStaff()));
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
}
