package model;

import model.characters.GameCharacter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HallOfFameData extends ArrayList<HallOfFameEntry> implements Serializable {

    public void append(Party party, GameScore score) {
        List<GameCharacter> chars = new ArrayList<>();
        for (GameCharacter gc : party.getPartyMembers()) { // moving leader first.
            if (!gc.isLeader()) {
                chars.add(gc);
            }
        }
        chars.add(0, party.getLeader());
        HallOfFameEntry e = new HallOfFameEntry(chars, score.getTotal());
        add(e);
        Collections.sort(this, (e1, e2) -> e2.getScore() - e1.getScore());
    }
}
