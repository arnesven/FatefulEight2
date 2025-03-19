package model.states.events;

import model.Model;
import model.characters.PersonalityTrait;
import model.combat.CombatAdvantage;
import model.enemies.Enemy;
import model.enemies.GhostEnemy;
import model.enemies.VampireEnemy;
import model.states.DailyEventState;
import util.MyRandom;
import view.combat.DungeonTheme;

import java.util.ArrayList;
import java.util.List;

public class VampireCryptEvent extends DailyEventState {
    public VampireCryptEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        boolean ambush = MyRandom.flipCoin();
        println("The party " + (ambush?"is ambushed by":"encounters") + " a vampire!");
        List<Enemy> result = new ArrayList<>();
        int num = getSuggestedNumberOfEnemies(getModel(), new VampireEnemy('A'));
        for (int i = num; i > 0; --i) {
            result.add(new VampireEnemy('A'));
        }
        runCombat(result, new DungeonTheme(), true,
                ambush ? CombatAdvantage.Enemies : CombatAdvantage.Neither, new ArrayList<>());
    }
}
