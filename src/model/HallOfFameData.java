package model;

import model.characters.GameCharacter;
import util.MyLists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HallOfFameData extends ArrayList<HallOfFameEntry> implements Serializable {

    public void append(Party party, GameScore score) {
        List<GameCharacter> chars = MyLists.filter(party.getPartyMembers(), (GameCharacter gc) -> !gc.isLeader());
        chars.add(0, party.getLeader());
        HallOfFameEntry e = new HallOfFameEntry(chars, score.getTotal());
        add(e);
        Collections.sort(this, (e1, e2) -> e2.getScore() - e1.getScore());
    }
}
