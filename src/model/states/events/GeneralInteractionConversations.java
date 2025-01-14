package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.map.CastleLocation;
import model.map.HexLocation;
import model.map.TownLocation;
import model.map.UrbanLocation;
import model.races.Race;
import model.states.GameState;
import util.MyStrings;

import java.awt.*;

public class GeneralInteractionConversations {
    public static String getReplyFor(Model model, GameCharacter victim, String topic) {
        topic = topic.toLowerCase();
        String answer = checkForWorldQuestions(model, topic);
        if (answer != null) {
            return answer;
        }
        answer = checkForRaceQuestions(model, victim, topic);
        return answer;
    }

    private static String checkForWorldQuestions(Model model, String topic) {
        for (UrbanLocation urb : model.getWorld().getLordLocations()) {
            if (urb.getPlaceName().toLowerCase().contains(topic)) {
                if (model.getWorld().getHex(model.getParty().getPosition()).getLocation() == urb) {
                    String townOrCastle = urb instanceof TownLocation ? "town" : "castle";
                    return "It's the " + townOrCastle + " we are currently in.";
                }
                String result;
                if (urb instanceof TownLocation) {
                    Point pos = model.getWorld().getPositionForLocation((HexLocation) urb);
                    result = MyStrings.capitalize(urb.getPlaceName()) + "? It's a town in the " +
                            CastleLocation.placeNameToKingdom(model.getWorld().getKingdomForPosition(pos).getPlaceName()) + ". " +
                            urb.getGeographicalDescription();
                } else {
                    result = "It's a kingdom, ruled from the castle by the same name. " +
                            urb.getGeographicalDescription();
                }
                return result;
            }
            if (urb.getLordName().toLowerCase().contains(topic)) {
                return urb.getLordName() + "? " +
                        GameState.heOrSheCap(urb.getLordGender()) + " is the ruler of the " +
                        CastleLocation.placeNameToKingdom(urb.getPlaceName()) + ".";
            }
        }
        if (topic.equals("thelnius")) {
            return "It's a river running through both Upper and Lower Theln.";
        }
        if (topic.equals("Ojai")) {
            return "It's a river. Ebonshire and Little Erinde lie on it.";
        }
        if (topic.equals("Zind")) {
            return "It's a desert, it lies north of Castle Ardh.";
        }
        if (topic.equals("Drize")) {
            return "It's a desert, it lies north of Castle Sunblaze.";
        }
        return null;
    }

    private static String checkForRaceQuestions(Model model, GameCharacter victim, String topic) {
        if (victim.getRace().getName().toLowerCase().equals(topic) ||
                victim.getRace().getPlural().toLowerCase().equals(topic)) {
            return "Yes, I'm a " + victim.getRace().getName() + ". What of it?";
        }
        for (Race race : Race.allRaces) {
            if (race.getName().toLowerCase().equals(topic) ||
                race.getPlural().toLowerCase().equals(topic)) {
                String attitude = "";
                if (victim.getRace().getInitialAttitudeFor(race) < -1) {
                    attitude = " Don't much care for them really.";
                }
                return race.getPlural() + "? They're " + race.getShortDescription() + "." + attitude;
            }
        }
        return null;
    }
}
