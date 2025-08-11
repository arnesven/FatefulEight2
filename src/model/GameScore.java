package model;

import java.util.HashMap;

public class GameScore extends HashMap<String, Integer> {

    public static GameScore calculate(Model model) {
        GameScore gs = new GameScore();
        gs.put("Achievements", model.getAchievements().numberOfCompleted(model) * 1000);
        gs.put("Main Story Completed", model.getMainStory().isCompleted(model)?2500:0);
        return gs;
    }

    public int getTotal() {
        int sum = 0;
        for (Integer i : values()) {
            sum += i;
        }
        return sum;
    }
}
