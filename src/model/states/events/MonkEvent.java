package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.AdvancedAppearance;
import model.classes.Classes;
import model.enemies.Enemy;
import model.items.Equipment;
import model.items.clothing.PilgrimsCloak;
import model.items.weapons.LongStaff;
import model.map.HexLocation;
import model.states.DailyEventState;
import model.states.dailyaction.AcceptTravellerState;
import model.travellers.Traveller;
import util.MyRandom;
import view.subviews.PortraitSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MonkEvent extends DarkDeedsEvent {
    private AdvancedAppearance portrait;

    public MonkEvent(Model model) {
        super(model, "Talk To", MyRandom.randInt(4, 8));
    }

    @Override
    protected boolean doIntroAndContinueWithEvent(Model model) {
        this.portrait = PortraitSubView.makeRandomPortrait(Classes.PRI);
        println("You meet a monk on the road.");
        showExplicitPortrait(model, portrait, "Monk");
        return true;
    }

    @Override
    protected boolean doMainEventAndShowDarkDeeds(Model model) {
        GameCharacter victim = getVictimCharacter(model);
        model.getWorld().dijkstrasByLand(model.getParty().getPosition(), false);
        List<Point> path = model.getWorld().shortestPathToNearestTemple();
        HexLocation location = model.getWorld().getHex(path.get(path.size()-1)).getLocation();
        Traveller traveller = new Traveller(victim.getName(), victim.getAppearance(), location, path.size());
        AcceptTravellerState accept = new AcceptTravellerState(model, this, traveller);
        accept.run(model);
        return !model.getParty().getActiveTravellers().contains(traveller);
    }

    @Override
    protected GameCharacter getVictimCharacter(Model model) {
        GameCharacter monk = new GameCharacter("Monk", "", portrait.getRace(), Classes.PRI, portrait,
                Classes.NO_OTHER_CLASSES, new Equipment(new LongStaff()));
        monk.setLevel(MyRandom.randInt(1, 4));
        return monk;
    }

    @Override
    protected List<Enemy> getVictimCompanions(Model model) {
        return new ArrayList<>();
    }

    @Override
    protected ProvokedStrategy getProvokedStrategy() {
        return ProvokedStrategy.FIGHT_IF_ADVANTAGE;
    }
}
