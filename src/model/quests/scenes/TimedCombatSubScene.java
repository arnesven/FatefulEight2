package model.quests.scenes;

import model.Model;
import model.enemies.Enemy;
import model.quests.QuestEdge;
import model.states.QuestState;

import java.util.List;

public class TimedCombatSubScene extends CombatSubScene {
    public TimedCombatSubScene(int col, int row, List<Enemy> enemies, boolean fleeingEnabled, int turns) {
        super(col, row, enemies, fleeingEnabled);
        setTimeLimit(turns);
    }

    @Override
    public QuestEdge run(Model model, QuestState state) {
        return super.run(model, state);
    }

    @Override
    protected String getCombatDetails() {
        return "Werewolf";
    }
}
