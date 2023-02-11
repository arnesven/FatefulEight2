package model.states.events;

import model.Model;
import model.classes.Skill;
import model.enemies.Enemy;
import model.enemies.OrcWarrior;
import model.states.DailyEventState;

import java.util.ArrayList;
import java.util.List;

public class OrcBandEvent extends DailyEventState {
    public OrcBandEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("The party is taking a little rest by the side of the road " +
                "when they hear footsteps approaching, many footsteps. " +
                "It's a whole band of orcs! It may be possible for the party " +
                "to hide behind some bushes and remain undetected.");
        // TODO: add bonus if you have half orc(s)?
        boolean result = model.getParty().doCollectiveSkillCheck(model, this, Skill.Sneak, 5);
        if (result) {
            println("You stay hidden as the throng passes by.");
            // TODO: Follow them ? to -> Orc Stronghold / Campsite / Ambush?
        } else {
            List<Enemy> enemies = new ArrayList<>();
            for (int i = 0; i < 6; ++i) {
                enemies.add(new OrcWarrior('A'));
            }
            runCombat(enemies);
        }
    }
}
