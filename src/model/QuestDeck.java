package model;

import model.map.HexLocation;
import model.quests.DeepDungeonQuest;
import model.quests.MansionHeistQuest;
import model.quests.Quest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuestDeck extends ArrayList<Quest> implements Serializable {

    private Map<Quest, HexLocation> acceptedQuests = new HashMap<>();

    public Quest getRandomQuest() {
        return new MansionHeistQuest();
        //return new DeepDungeonQuest();
    }

    public void accept(Quest quest, HexLocation location) {
        acceptedQuests.put(quest, location);
    }

    public boolean alreadyDone(HexLocation location) {
        return acceptedQuests.values().contains(location);
    }
}
