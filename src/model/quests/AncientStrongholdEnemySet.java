package model.quests;

import model.Party;
import model.combat.CombatLoot;
import model.enemies.*;
import model.items.special.PearlItem;
import util.MyPair;
import util.MyRandom;
import view.MyColors;

import java.util.ArrayList;
import java.util.List;

public class AncientStrongholdEnemySet {
    private final List<Enemy> enemies;
    private final MyColors pearlColor;

    public AncientStrongholdEnemySet(int floorNumber) {
        MyPair<List<Enemy>, MyColors> pair = makeEnemiesForFloor(floorNumber);
        this.enemies = pair.first;
        this.pearlColor = pair.second;
    }

    public static MyPair<List<Enemy>, MyColors> makeEnemiesForFloor(int floorNumber) {
        if (floorNumber == 0) {
            int dieRoll = MyRandom.rollD10();
            return innerMakeEnemies(dieRoll);
        }
        return innerMakeEnemies(floorNumber);

    }

    private static MyPair<List<Enemy>, MyColors> innerMakeEnemies(int val) {
        switch (val) {
            case 1 : return makeFrogmanEnemies();
            case 2 : return makeOrcEnemies();
            case 3 : return makeBeastEnemies();
            case 4 : return makeUndeadEnemies();
            case 5 : return makeDemonicEnemies();
            case 6 : return makeGoblinEnemies();
            default: return makeMixedEnemies();
        }
    }

    private static MyPair<List<Enemy>, MyColors> makeMixedEnemies() {
        int dieRoll = MyRandom.rollD10();
        switch (dieRoll) {
            case 1 :
            case 2 : return new MyPair<>(List.of(new GiantRatEnemy('A'), new GiantRatEnemy('A'), new GiantRatEnemy('A'),
                    new GiantRatEnemy('A'), new GiantRatEnemy('A'), new SpiderEnemy('B'), new SpiderEnemy('B'), new SpiderEnemy('B')),
                    MyColors.WHITE);
            case 3 :
            case 4 : return new MyPair<>(List.of(new ManticoreEnemy('A'), new ManticoreEnemy('A'), new ManticoreEnemy('A'),
                    new ScorpionEnemy('B'), new ScorpionEnemy('B')),
                    MyColors.WHITE);
            case 5 :
            case 6 : return new MyPair<>(List.of(new TrollEnemy('A'), new TrollEnemy('A')), MyColors.WHITE);
            case 7 :
            case 8 : return new MyPair<>(List.of(new FaeryEnemy('A'), new FaeryEnemy('A'), new FaeryEnemy('A'), new FaeryEnemy('A'),
                    new FaeryEnemy('A'), new FaeryEnemy('A'), new FaeryEnemy('A'), new FaeryEnemy('A'), new FaeryEnemy('A')),
                    MyColors.WHITE);
            case 9:
            default : return new MyPair<>(List.of(new CrocodileEnemy('A'), new CrocodileEnemy('A'), new CrocodileEnemy('A'),
                    new SpiderEnemy('B'), new SpiderEnemy('B')),
                    MyColors.WHITE);
        }
    }

    private static MyPair<List<Enemy>, MyColors> makeGoblinEnemies() {
        if (MyRandom.flipCoin()) {
            return new MyPair<>(List.of(new GoblinSpearman('A'), new GoblinSpearman('A'),  new GoblinSpearman('A'),  new GoblinSpearman('A'),
                    new GoblinSpearman('A'),  new GoblinSpearman('A'),  new GoblinSpearman('A'), new GoblinBowman('B'), new GoblinBowman('B')),
                    MyColors.PURPLE);
        }
        return new MyPair<>(List.of(new GoblinClubWielder('A'), new GoblinClubWielder('A'), new GoblinClubWielder('A'),
                new GoblinBowman('B'), new GoblinBowman('B'), new GoblinBowman('B'), new GoblinBowman('B')),
                MyColors.PURPLE);
    }

    private static MyPair<List<Enemy>, MyColors> makeDemonicEnemies() {
        if (MyRandom.flipCoin()) {
            return new MyPair<>(List.of(new FiendEnemy('A'), new FiendEnemy('A'), new FiendEnemy('A'), new ImpEnemy('B'), new ImpEnemy('B')),
                    MyColors.ORANGE);
        }
        return new MyPair<>(List.of(new ImpEnemy('A'), new DaemonEnemy('B'), new SuccubusEnemy('C'), new ImpEnemy('A')),
                MyColors.ORANGE);
    }

    private static MyPair<List<Enemy>, MyColors> makeUndeadEnemies() {
        if (MyRandom.flipCoin()) {
            return new MyPair<>(List.of(new GhostEnemy('A'), new GhostEnemy('A'), new GhostEnemy('A'), new GhostEnemy('A'), new GhostEnemy('A')),
                    MyColors.BLACK);
        }
        return new MyPair<>(List.of(new SkeletonEnemy('A'), new SkeletonEnemy('A'), new SkeletonEnemy('A'),
                new SkeletonEnemy('A'),  new SkeletonEnemy('A'),  new SkeletonEnemy('A'), new SkeletonEnemy('A')),
                MyColors.BLACK);
    }

    private static MyPair<List<Enemy>, MyColors> makeBeastEnemies() {
        if (MyRandom.flipCoin()) {
            return new MyPair<>(List.of(new BatEnemy('A'), new BatEnemy('A'), new BatEnemy('A'),
                    new BatEnemy('A'), new BatEnemy('A'), new BatEnemy('A'), new BatEnemy('A'),
                    new GiantRatEnemy('B'), new GiantRatEnemy('B'), new GiantRatEnemy('B'), new GiantRatEnemy('B')),
                    MyColors.PINK);
        }
        return new MyPair<>(List.of(new WolfEnemy('A'), new WolfEnemy('A'), new WolfEnemy('A'), new WolfEnemy('A'),
                new BearEnemy('B'), new BearEnemy('B'), new WildBoarEnemy('C'), new WildBoarEnemy('C')),
                MyColors.PINK);
    }

    private static MyPair<List<Enemy>, MyColors> makeOrcEnemies() {
        if (MyRandom.flipCoin()) {
            return new MyPair<>(List.of(new OrcChieftain('A'), new OrcChieftain('A'), new OrcChieftain('A'),
                    new OrcArcherEnemy('B'), new OrcArcherEnemy('B'), new OrcArcherEnemy('B')),
                    MyColors.GREEN);
        }
        return new MyPair<>(List.of(new OrcWarrior('A'), new OrcWarrior('A'), new OrcArcherEnemy('B'), new OrcArcherEnemy('B'),
                new OrcChieftain('C')),
                MyColors.GREEN);
    }

    private static MyPair<List<Enemy>, MyColors> makeFrogmanEnemies() {
        if (MyRandom.flipCoin()) {
            return new MyPair<>(List.of(new FrogmanShamanEnemy('A'), new FrogmanShamanEnemy('A'), new FrogmanShamanEnemy('A'), new FrogmanShamanEnemy('A'),
            new FrogmanLeaderEnemy('B')), MyColors.BLUE);
        }
        return new MyPair<>(List.of(new FrogmanScoutEnemy('A'), new FrogmanScoutEnemy('A'), new FrogmanScoutEnemy('A'),
                new FrogmanLeaderEnemy('B'), new FrogmanShamanEnemy('C'), new FrogmanScoutEnemy('A')),
                MyColors.BLUE);
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<CombatLoot> getPearls() {
        List<CombatLoot> pearls = new ArrayList<>();
        for (int i = 0; i < enemies.size(); ++i) {
            if (MyRandom.rollD10() >= 7) {
                MyColors colorToMake = pearlColor;
                if (colorToMake == MyColors.WHITE) {
                    colorToMake = AncientStrongholdControlPanel.PEARL_COLORS[MyRandom.randInt(AncientStrongholdControlPanel.PEARL_COLORS.length-1)];
                }
                pearls.add(new PearlLoot(PearlItem.makeFromColor(colorToMake)));
            }
        }
        if (MyRandom.rollD10() == 10 && MyRandom.rollD10() == 10) {
            pearls.add(new PearlLoot(PearlItem.makeFromColor(MyColors.DARK_RED)));
        }
        return pearls;
    }

    private class PearlLoot extends CombatLoot {
        private final PearlItem pearl;

        public PearlLoot(PearlItem pearlItem) {
            this.pearl = pearlItem;
        }

        @Override
        public String getText() {
            return pearl.getName();
        }

        @Override
        protected void specificGiveYourself(Party party) {
            party.getInventory().addSpecialItem(pearl);
        }
    }
}
