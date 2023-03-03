package model.states.events;

import model.Model;
import model.enemies.DragonEnemy;
import model.states.DailyEventState;
import view.subviews.MountainCombatTheme;

import java.util.List;

public class DragonEvent extends DailyEventState {
    public DragonEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("Suddenly a powerful gust of wind catches the party off " +
                "guard. Then, the horror...");
        model.getParty().randomPartyMemberSay(model, List.of("DRAGON!!!"));
        runCombat(List.of(new DragonEnemy('A')), new MountainCombatTheme(), true);
    }

}
