package model.states;

import model.Model;
import model.characters.GameCharacter;
import model.items.accessories.Spyglass;
import model.map.Direction;
import model.map.HexLocation;
import model.map.WorldHex;
import model.states.events.NoEventState;
import util.MyLists;
import view.subviews.CollapsingTransition;
import view.subviews.MapSubView;
import view.subviews.SpyglassSubView;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class SpyglassState extends GameState {
    public SpyglassState(Model model) {
        super(model);
    }

    public static boolean hasSpyglass(Model model) {
        return MyLists.any(model.getParty().getInventory().getAllItems(), it -> it instanceof Spyglass) ||
                MyLists.any(model.getParty().getPartyMembers(), gc -> gc.getEquipment().getAccessory() instanceof Spyglass);
    }

    @Override
    public GameState run(Model model) {
        if (model.getParty().size() > 1) {
            GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
            partyMemberSay(other, "What can you see " + model.getParty().getLeader().getFirstName() + "?");
        } else {
            leaderSay("Let's see what we can see...");
        }
        println("Choose a hex to peer into with the Spyglass. Select your current hex when you are done using the Spyglass.");
        MapSubView subView = new SpyglassSubView(model);
        CollapsingTransition.transition(model, subView);
        Set<Integer> nothingInDirection = new HashSet<>();
        do {
            waitForReturnSilently();
            Point selectedDir = subView.getSelectedDirection();
            if (selectedDir.x == 0 && selectedDir.y == 0) {
                break;
            }
            Point newPosition = new Point(model.getParty().getPosition());
            model.getWorld().move(newPosition, selectedDir.x, selectedDir.y);

            WorldHex hex = model.getWorld().getHex(newPosition);
            if (hex.getLocation() != null && !hex.getLocation().isDecoration()) {
                HexLocation loc = hex.getLocation();
                leaderSay("I see the " + loc.getName() + ".");
            } else {
                int direction = Direction.getDirectionForDxDy(model.getParty().getPosition(), selectedDir);
                String directionName = Direction.getLongNameForDirection(direction);

                if (hex.getPreparedEvent(model.getDay()) != null) {
                    DailyEventState event = hex.getPreparedEvent(model.getDay());
                    leaderDescribeEvent(hex, event, directionName);
                } else {
                    DailyEventState event = hex.generateEventFromDistance(model);
                    if (nothingInDirection.contains(direction) ||
                            event instanceof NoEventState || event.getDistantDescription() == null) {
                        leaderSay("Just some " + hex.getTerrainName() + " to the " + directionName + ".");
                        nothingInDirection.add(direction);
                    } else {
                        leaderDescribeEvent(hex, event, directionName);
                        hex.pushPreparedEvent(event, model.getDay());
                    }
                }
            }
        } while (true);
        return model.getCurrentHex().getDailyActionState(model);
    }

    private void leaderDescribeEvent(WorldHex hex, DailyEventState event, String directionName) {
        String line = "In the " + hex.getTerrainName() + " to the " + directionName + " I see " +
                event.getDistantDescription() + ".";
        leaderSay(line);
    }
}
