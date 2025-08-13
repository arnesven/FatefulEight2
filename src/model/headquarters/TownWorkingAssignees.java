package model.headquarters;

import model.GameStatistics;
import model.Model;
import model.characters.GameCharacter;
import util.MyLists;
import util.MyRandom;

import java.util.ArrayList;

public class TownWorkingAssignees extends ArrayList<GameCharacter> {
    public static final int MAX_GOLD_FROM_WORK = 3;

    public void performAssignments(Model model, Headquarters headquarters, StringBuilder logEntry) {
        if (!isEmpty()) {
            for (GameCharacter worker : this) {
                worker.addToSP(-MyRandom.randInt(1, 2));
            }
            logEntry.append(MyLists.commaAndJoin(this, GameCharacter::getName));
            logEntry.append(" did work in town, ");
            if (size() == 1 && MyRandom.rollD10() == 1) {
                logEntry.append("got beaten up by robbers!");
                get(0).addToHP(2 - get(0).getHP());
            } else {
                int goldGenerated = MyRandom.randInt(0, size() * MAX_GOLD_FROM_WORK);
                logEntry.append("earned ").append(goldGenerated).append(" gold.\n");
                headquarters.addToGold(goldGenerated);
                GameStatistics.incrementGoldEarned(goldGenerated);
            }
        }
    }
}
