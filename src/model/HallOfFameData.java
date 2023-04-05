package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HallOfFameData extends ArrayList<HallOfFameEntry> implements Serializable {

    public void append(Party party, GameScore score) {
        HallOfFameEntry e = new HallOfFameEntry(party.getPartyMembers(), score.getTotal());
        add(e);
        Collections.sort(this, Comparator.comparingInt(HallOfFameEntry::getScore));
    }
}
