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
import java.util.HashMap;
import java.util.Map;

public class GeneralInteractionConversations {

    private static final Map<String, String> MONSTER_MAP = makeMonsterMap();

    public static String getReplyFor(Model model, GameCharacter victim, String topic) {
        topic = topic.toLowerCase();
        String answer = checkForWorldQuestions(model, topic);
        if (answer != null) {
            return answer;
        }
        answer = checkForRaceQuestions(model, victim, topic);
        if (answer != null) {
            return answer;
        }
        answer = checkForMonsterQuestions(model, topic);
        if (answer != null) {
            return answer;
        }
        answer = checkForTravelQuestions(model, topic);
        if (answer != null) {
            return answer;
        }
        answer = checkForMiscQuestions(model, topic);
        return answer;
    }

    private static String checkForTravelQuestions(Model model, String topic) {
        if (topic.equals("horse") || topic.equals("horses") || topic.equals("pony") || topic.equals("ponies")) {
            return "If you have a horse to ride on, you'll travel faster.";
        }
        if (topic.equals("boat") || topic.equals("ship")) {
            return "You can sometimes take a boat from a coastal town to another, for a small fee of course. " +
                    "Sometimes you may even be able to charter a boat, but that would be more expensive.";
        }
        if (topic.equals("cart") || topic.equals("wagon") || topic.equals("carriage")) {
            return "You can travel by carriage between some towns. " +
                    "It costs a little, but it's a comfortable way to travel. " +
                    "Sometimes farmers will give you a ride for free.";
        }
        return null;
    }

    private static String checkForMiscQuestions(Model model, String topic) {
        if (topic.equals("runny") || topic.equals("knock-out")) {
            return "It's a card game. It's played at inns and taverns.";
        }
        if (topic.equals("dog") || topic.equals("dogs")) {
            return "There are plenty of dogs in towns. Dogs also sometimes appear out in the wild.";
        }
        if (topic.equals("brotherhood") || topic.equals("the brotherhood")) {
            return "The brotherhood is an organized crime network. They're really a shady bunch.";
        }
        if (topic.equals("crafting")) {
            return "Crafting benches can be found in towns and castles. " +
                    "You'll need some materials if you want to make something though.";
        }
        if (topic.equals("alchemy")) {
            return "It's a spell right? You'll need ingredients to brew potions.";
        }
        if (topic.equals("fishing")) {
            return "Do you have a fishing pole? Fish are almost everywhere... everywhere there's water that is.";
        }
        if (topic.equals("ingredients")) {
            return "The best place to find some ingredients is in the forest, on the plains or in caves. " +
                    "But I bet you can find even more if you go to more exotic places like swamps and jungles.";
        }
        if (topic.equals("materials")) {
            return "The best place to find materials is in the mountains, " +
                    "but hilly areas and caves are often good too.";
        }
        return null;
    }

    private static String checkForWorldQuestions(Model model, String topic) {
        for (UrbanLocation urb : model.getWorld().getLordLocations()) {
            if (urb.getPlaceName().toLowerCase().contains(topic)) {
                if (model.getWorld().getHex(model.getParty().getPosition()).getLocation() == urb) {
                    return "It's the " + urb.getLocationType() + " we are currently in.";
                }
                String result = null;
                if (urb instanceof TownLocation) {
                    Point pos = model.getWorld().getPositionForLocation((HexLocation) urb);
                    result = MyStrings.capitalize(urb.getPlaceName()) + "? It's a town in the " +
                            CastleLocation.placeNameToKingdom(model.getWorld().getKingdomForPosition(pos).getPlaceName()) + ". " +
                            urb.getGeographicalDescription();
                } else if (urb instanceof CastleLocation) {
                    result = "It's a kingdom, ruled from the castle by the same name. " +
                            urb.getGeographicalDescription();
                } // Other urban location will give "Dunno" answer.
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

    private static String checkForMonsterQuestions(Model model, String topic) {
        if (MONSTER_MAP.containsKey(topic)) {
            return MONSTER_MAP.get(topic);
        }
        if (MONSTER_MAP.containsKey(topic + "s")) {
            return MONSTER_MAP.get(topic + "s");
        }
        return null;
    }

    private static Map<String, String> makeMonsterMap() {
        Map<String, String> map = new HashMap<>(Map.of(
                "bats",
                "Bats live in caves underground. They're pretty harmless, unless they attack you en masse.",
                "bears",
                "Bears live in the woods or the mountains. They can be very dangerous.",
                "snakes",
                "Snakes live in woods or swamps. Some kinds are poisonous.",
                "crocodiles",
                "Crocodiles only live in swamps. They're pretty terrifying beasts.",
                "dragons",
                "Dragons are fearsome creatures. If you try to fight one, you better come prepared.",
                "faery",
                "Faeries are fickle critters. The ones I encountered almost acted like they could read my mind."
                ));

        map.putAll(Map.of(
                "frogmen",
                "Frogmen are near-intelligent creatures. They have their own language and everything.",
                "ghosts",
                "Ghosts are scary. I try to stay clear of haunted houses.",
                "ghouls",
                "Ghouls are undead, zombie-like creatures. Pretty horrific if you ask me.",
                "goblins",
                "Goblins can be very dangerous if many of them come after you with their weapons drawn.",
                "lizardmen",
                "Lizardmen are an uncanny race of humanoids. I've only met a few, but I can't say I trust them.",
                "manticores",
                "Manticores are dangerous monsters who live in deserts.",
                "ogres",
                "Ogers are larger than orcs, and dumber too. If you meet one, don't upset it!",
                "orcs",
                "Orcs are a malicious race of muscular humanoids who often pillage and plunder innocent villages."
        ));

        map.putAll(Map.of(
                "rats",
                "Rats live in old basements and dungeons. They're not very dangerous, but the big ones can be a handful to exterminate.",
                "scorpions",
                "Scorpions can be found in deserts. Their stingers are poisonous.",
                "skeletons",
                "I've heard skeletons sometimes guard tombs and crypts. I'd be freaked out if I ever met one.",
                "spiders",
                "Spiders live in forests, caves and many other places. Their attacks can paralyze you, be careful.",
                "trolls",
                "Trolls are larger than orcs, even larger than ogres! They are very dangerous, but can somtimes be reasoned with.",
                "werewolves",
                "Werewolves are normal people who turn into half-wolves during full moon nights. Their howls are terrifying.",
                "boars",
                "Wild boars are dangerous beasts, but their meat is delicious when roasted over an open fire.",
                "wolves",
                "Wolves hunt in packs. Don't forget to have somebody keep watch if you're trekking through the wilds."
        ));
        return map;
    }
}
