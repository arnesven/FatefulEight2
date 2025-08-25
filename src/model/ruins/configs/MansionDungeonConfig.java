package model.ruins.configs;

import model.Model;
import model.enemies.BanditArcherEnemy;
import model.enemies.BanditLeaderEnemy;
import model.enemies.BrotherhoodCronyEnemy;
import model.enemies.Enemy;
import model.ruins.factories.MonsterFactory;
import model.ruins.objects.DungeonMonster;
import model.ruins.themes.HouseDungeonTheme;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MansionDungeonConfig extends DungeonLevelConfig {
    private static final double NO_CORPSES = 0.0;
    private static final double MONSTERS = 0.2;
    private static final double LOCKED_DOORS = 0.08;
    private static final double NO_SPIKE_TRAPS = 0.0;
    private static final double NO_PITFALL_TRAPS = 0.0;
    private static final double NO_CAMPFIRES = 0.0;
    private static final double CORRIDORS = 1.0;
    private static final double CRACKED_WALLS = 0.125;
    private static final double CHESTS = 0.4;
    private static final double LEVERS = 0.4;

    public MansionDungeonConfig() {
        super(new HouseDungeonTheme(), new MansionMonsterFactory(), 
                CHESTS, LEVERS, NO_CORPSES, MONSTERS, LOCKED_DOORS, NO_SPIKE_TRAPS,
                NO_PITFALL_TRAPS, NO_CAMPFIRES, CORRIDORS, CRACKED_WALLS);
    }

    private static class MansionMonsterFactory extends MonsterFactory {
        @Override
        protected DungeonMonster spawnMonster(Model model, Random random) {
            List<Enemy> enms = new ArrayList<>();
            for (int i = MyRandom.randInt(1, 5); i > 0; i--) {
                enms.add(new BrotherhoodCronyEnemy('B'));
            }
            for (int i = MyRandom.randInt(1, 4); i > 0; i--) {
                enms.add(new BanditArcherEnemy('A'));
            }
            if (MyRandom.flipCoin()) {
                enms.add(new BanditLeaderEnemy('C'));
            }
            return new MansionThugMonster(enms);
        }
    }

    private static class MansionThugMonster extends DungeonMonster {
        private String numberWord;

        public MansionThugMonster(List<Enemy> enms) {
            super(enms);
            if (enms.size() <= 2) {
                numberWord = "A pair";
            } else if (enms.size() <= 4) {
                numberWord = "A small group";
            } else {
                numberWord = "A large group";
            }
        }

        @Override
        public String getDescription() {
            return numberWord + " of Brotherhood thugs";
        }
    }
}
