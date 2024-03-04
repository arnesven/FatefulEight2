package model.journal;

import model.Model;

import java.awt.*;

public abstract class PositionlessJournalEntry implements JournalEntry {

    @Override
    public Point getPosition(Model model) {
        return null;
    }
}
