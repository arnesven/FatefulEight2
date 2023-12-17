package model.journal;

import model.Model;
import model.map.RuinsLocation;
import model.ruins.RuinsDungeon;
import util.MyPair;

import java.util.ArrayList;


public class RuinsEntry implements JournalEntry {
    private final ArrayList<MyPair<RuinsLocation, RuinsDungeon>> ruins;
    private boolean allCompleted;

    public RuinsEntry(Model model) {
        ruins = new ArrayList<>();
        allCompleted = true;
        for (RuinsLocation ruinsLocation : model.getWorld().getRuinsLocations()) {
            MyPair<RuinsLocation, RuinsDungeon> pair = new MyPair<>(ruinsLocation, getDungeon(model, ruinsLocation.getName(), true));
            ruins.add(pair);
            if (pair.second == null || !pair.second.isCompleted()) {
                allCompleted = false;
            }
        }
    }

    @Override
    public boolean isTask() {
        return true;
    }

    private RuinsDungeon getDungeon(Model model, String name, boolean isRuins) {
        if (model.hasVisitedDungeon(name)) {
            return model.getDungeon(name, isRuins);
        }
        return null;
    }

    @Override
    public String getName() {
        return "Explore Ruins";
    }

    @Override
    public String getText() {
        StringBuilder bldr = new StringBuilder();
        bldr.append("Once you have a larger, well equipped and experienced party, " +
                "you should seek out some ruins and explore them.\n\n");
        for (MyPair<RuinsLocation, RuinsDungeon> ruin : ruins) {
            bldr.append(" " + ruin.first.getName());
            if (ruin.second != null) {
                if (ruin.second.isCompleted()) {
                    bldr.append(" - Cleared!");
                } else {
                    bldr.append(" - Visited");
                }
            }
            bldr.append("\n");
        }
        return bldr.toString();
    }

    @Override
    public boolean isComplete() {
        return allCompleted;
    }

    @Override
    public boolean isFailed() {
        return false;
    }
}
