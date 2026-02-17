package model.states.dailyaction;

import model.Model;
import model.map.UrbanLocation;
import model.map.locations.JungleVillageLocation;
import model.states.GameState;
import model.states.dailyaction.tavern.JungleTavernNode;
import model.states.dailyaction.town.CharterBoatAtDocks;
import view.MyColors;

import java.awt.*;

public class JungleVillageDailyActionState extends TownishDailyActionState {
    public JungleVillageDailyActionState(Model model, JungleVillageLocation jungleVillageLocation) {
        super(model, true, jungleVillageLocation, false, false);
        addNode(6, 3, new JequensHutNode());
        Point p = jungleVillageLocation.getTavernPosition();
        p.y = p.y - 1;
        blockPosition(p.x, p.y);
    }

    protected DailyActionNode makeStayHereNode() {
        return new JungleVillageStayHereNode();
    }

    @Override
    public void addTavernNode(Model model, boolean freeLodging, UrbanLocation urbanLocation) {
        blockPosition(4, 3);
        addNode(4, 4, new JungleTavernNode(freeLodging));
    }

    @Override
    protected void addTravelNodes(Model model, boolean hasWaterAccess, UrbanLocation urbanLocation) {
        super.addTravelNodes(model, hasWaterAccess, urbanLocation);
        addNode(3, 0, new CharterBoatAtDocks(model));
    }

    private static class JungleVillageStayHereNode extends StayHereNode {
        @Override
        public void setTimeOfDay(Model model, AdvancedDailyActionState state) {
            // Intentionally left empty
        }
    }
}
