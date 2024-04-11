package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.characters.appearance.CharacterAppearance;
import model.classes.Classes;
import model.enemies.Enemy;
import model.items.Equipment;
import model.items.clothing.PilgrimsCloak;
import model.items.weapons.LongStaff;
import model.items.weapons.ShortSword;
import model.map.HexLocation;
import model.states.DailyEventState;
import model.states.dailyaction.AcceptTravellerState;
import model.travellers.Traveller;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PilgrimEvent extends MeetTravellerEvent {
    public PilgrimEvent(Model model) {
        super(model, makePilgrim(), MyRandom.randInt(5, 10), ProvokedStrategy.ALWAYS_ESCAPE,
                MyRandom.randInt(15, 35));
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
}
