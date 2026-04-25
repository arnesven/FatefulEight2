package model.quests;

import model.Model;
import model.map.HexLocation;
import model.map.UrbanLocation;
import util.MyLists;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestHandler implements Serializable {
    private final Map<String, OfferedQuest> offeredQuests = new HashMap<>();

    public void offerQuest(Model model, Quest q) {
        List<Point> path = q.makeRemotePath(model);
        HexLocation loc = model.getWorld().getHex(path.getFirst()).getLocation();
        String placeName = null;
        if (loc instanceof UrbanLocation) {
            placeName = ((UrbanLocation) loc).getPlaceName();
        }
        offeredQuests.put(q.getName(), new OfferedQuest(q.getName(),
                q.getPortrait(), path, placeName, q.getWorldOrigin(), q.getProvider()));
    }

    public List<OfferedQuest> getOfferedQuestsForPosition(Model model) {
        return MyLists.filter(getOfferedQuestsAsList(),
                oq -> oq.isPartyInOfferPosition(model) ||
                        (oq.isAccepted() && oq.isPartyInRemotePosition(model)));
    }

    public OfferedQuest getOfferedQuest(String name) {
        return offeredQuests.get(name);
    }

    public void accept(Model model, Quest q) {
        offeredQuests.get(q.getName()).accept(model.getDay());
    }

    public void completeQuest(Quest quest) {
        offeredQuests.get(quest.getName()).setCompleted(true);
    }

    public List<OfferedQuest> getOfferedQuestsAsList() {
        return new ArrayList<>(offeredQuests.values());
    }
}
