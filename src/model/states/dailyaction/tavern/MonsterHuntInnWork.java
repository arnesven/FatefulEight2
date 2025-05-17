package model.states.dailyaction.tavern;

import model.Model;
import model.map.CastleLocation;
import model.map.TownLocation;
import model.states.events.MonsterHuntEvent;

import java.awt.*;
import java.util.List;

public class MonsterHuntInnWork extends InnWorkAction {
    public MonsterHuntInnWork() {
        super("Monster Hunt", "Some townsfolk are are being terrorized by a monster, maybe you can deal with it?");
    }

    @Override
    public void doWork(Model model, TalkToBartenderState state) {
        model.getWorld().dijkstrasByLand(model.getParty().getPosition());
        boolean isAtCastle = model.getCurrentHex().getLocation() instanceof CastleLocation;
        List<Point> path = model.getWorld().generalShortestPath(isAtCastle ? 1 : 0, wh -> wh.getLocation() instanceof TownLocation);
        TownLocation town = (TownLocation) model.getWorld().getHex(path.getLast()).getLocation();

        state.bartenderSay(model, "I got this notice from the " + town.getLordTitle() +
                " in " + town.getTownName() + ".");
        state.println("The bartender hands you a note.");
        new MonsterHuntEvent(model, town, false).doTheEvent(model);
    }
}
