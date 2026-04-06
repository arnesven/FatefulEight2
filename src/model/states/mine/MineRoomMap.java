package model.states.mine;

import java.util.HashMap;
import java.util.Map;

public class MineRoomMap {
    private Map<String, MineRoom> map = new HashMap<>();

    public boolean roomExists(MineRoomLocation loc) {
        return map.containsKey(loc.asString());
    }

    public void put(MineRoomLocation loc, MineRoom room) {
        map.put(loc.asString(), room);
    }

    public MineRoom get(MineRoomLocation loc) {
        return map.get(loc.asString());
    }
}
