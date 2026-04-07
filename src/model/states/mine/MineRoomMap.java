package model.states.mine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MineRoomMap {
    private Map<String, MineRoom> map = new HashMap<>();
    private Set<MineRoom> discovered = new HashSet<>();

    public boolean roomExists(MineRoomLocation loc) {
        return map.containsKey(loc.asString());
    }

    public void put(MineRoomLocation loc, MineRoom room) {
        map.put(loc.asString(), room);
    }

    public MineRoom get(MineRoomLocation loc) {
        return map.get(loc.asString());
    }

    public boolean isRoomDiscovered(MineRoomLocation loc) {
        MineRoom r = get(loc);
        return r != null && discovered.contains(r);
    }

    public void setDiscovered(MineRoom room) {
        discovered.add(room);
    }
}
