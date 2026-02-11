package model.mainstory.thepast;

import model.journal.JournalEntry;
import model.map.WorldType;

public abstract class ThePastJournalEntry implements JournalEntry {
    @Override
    public WorldType getWorld() {
        return WorldType.thePast;
    }
}
