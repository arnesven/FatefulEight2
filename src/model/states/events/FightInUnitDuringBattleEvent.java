package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.appearance.RandomAppearance;
import model.classes.Classes;
import model.combat.Combatant;
import model.combat.loot.CombatLoot;
import model.combat.loot.PersonCombatLoot;
import model.enemies.*;
import model.items.Equipment;
import model.items.accessories.*;
import model.items.clothing.LeatherArmor;
import model.items.clothing.OutlawArmor;
import model.items.clothing.ScaleArmor;
import model.items.weapons.Longsword;
import model.items.weapons.ShortBow;
import model.map.CastleLocation;
import model.races.AllRaces;
import model.races.Race;
import model.states.CombatEvent;
import model.states.GameState;
import model.states.battle.*;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class FightInUnitDuringBattleEvent extends GameState {
    private final List<BattleUnit> enemyUnits;
    private final String origin;
    private final List<GameCharacter> allies;
    private boolean victorious = false;

    public FightInUnitDuringBattleEvent(Model model, String origin, List<BattleUnit> enemies) {
        super(model);
        this.origin = CastleLocation.placeNameShort(origin);
        this.enemyUnits = new ArrayList<>(enemies);
        this.allies = makeAllies(model);
    }

    private List<GameCharacter> makeAllies(Model model) {
        int numberOfAllies = MyRandom.randInt(5, 10);
        int dieRoll = MyRandom.rollD10();
        List<GameCharacter> allies = new ArrayList<>();
        for (int i = 0; i < numberOfAllies; ++i) {
            int raceIndex = MyRandom.randInt(AllRaces.allRaces.length);
            if (dieRoll < 3) {
                allies.add(new GameCharacter(origin + " Militia", "", AllRaces.allRaces[raceIndex], Classes.CAP, new RandomAppearance(AllRaces.allRaces[raceIndex]),
                        Classes.NO_OTHER_CLASSES,
                        new Equipment(new Longsword(), new LeatherArmor(), new SkullCap())));
            } else if (dieRoll < 6) {
                allies.add(new SpearmanCharacter(origin + " Spearman", "", AllRaces.allRaces[raceIndex]));
            } else if (dieRoll < 9) {
                allies.add(new GameCharacter(origin + " Archer", "", AllRaces.allRaces[raceIndex], Classes.MAR, new RandomAppearance(AllRaces.allRaces[raceIndex]),
                        Classes.NO_OTHER_CLASSES,
                        new Equipment(new ShortBow(), new OutlawArmor(), new LeatherCap())));
            } else {
                allies.add(new GameCharacter(origin + " Swordsman", "", AllRaces.allRaces[raceIndex], Classes.PAL, new RandomAppearance(AllRaces.allRaces[raceIndex]),
                        Classes.NO_OTHER_CLASSES,
                        new Equipment(new Longsword(), new ScaleArmor(), new HeraldicShield())));
            }
        }
        return allies;
    }

    @Override
    public GameState run(Model model) {
        println("The party heads down toward the battlefield just as the battle commences. The units which had seem so " +
                "orderly moments ago now clash into each other and plunge the scene in to utter chaos. In the confusion, some soldiers " +
                "from the unit join you as you search for the enemy.");
        leaderSay("Come on, everybody!");
        for (int count = 0; count < 3 && !enemyUnits.isEmpty(); count++) {
            BattleUnit unit = enemyUnits.remove(0);
            List<Enemy> enemies = makeEnemyFromUnit(unit);
            println("You spot a unit of " + unit.getName().toLowerCase() + ", they charge you!");
            leaderSay(MyRandom.sample(List.of("Everybody, stand your ground!", "Get ready!", "Keep together now.", "Courage!")));
            CombatEvent combat = new CombatEvent(model, enemies, model.getCurrentHex().getCombatTheme(), true, false);
            combat.addAllies(allies);
            combat.run(model);
            if (combat.haveFledCombat() || model.getParty().isWipedOut()) {
                victorious = false;
                return model.getCurrentHex().getEveningState(model, false, false);
            }
            allies.removeIf(Combatant::isDead);
            setCurrentTerrainSubview(model);
            leaderSay("That was a tough fight...");
            println("You barely have time to catch your breath before another group of enemies approach.");
        }
        victorious = true;
        return model.getCurrentHex().getEveningState(model, false, false);
    }

    private List<Enemy> makeEnemyFromUnit(BattleUnit unit) {
        List<Enemy> enemies = new ArrayList<>();
        for (int i = 0; i < Math.min(unit.getCount(), 24); ++i) {
            enemies.add(unit.makeEnemy());
        }
        return enemies;
    }

    public boolean isVictorious() {
        return victorious;
    }

}
