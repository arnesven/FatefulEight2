package model.states.battle;

import model.Model;
import model.map.Direction;
import util.MyLists;
import util.MyPair;
import util.MyStrings;
import view.subviews.BattleSubView;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ImprovedBattleAI extends BattleAI {

    private static final List<BattleDirection> DIRECTION_LIST =
            List.of(BattleDirection.east, BattleDirection.west, BattleDirection.north, BattleDirection.south);
    private static final int IMPASSIBLE_TERRAIN_COST = 10000000;
    private HashMap<BattleUnit, Integer> triesMap;

    @Override
    protected void startTurnHook(Model model, BattleSubView subView, BattleState battleState,
                                 ArrayList<BattleUnit> currentUnits) {
        triesMap = new HashMap<>();
    }

    @Override
    protected void specificActivateUnit(Model model, BattleSubView subView, BattleState battleState,
                                        ArrayList<BattleUnit> currentUnits) {
        BattleUnit currentUnit = currentUnits.get(0);
        if (currentUnit.getCount() == 0 || currentUnit.getMP() == 0 ||
                !battleState.isOnBattlefield(currentUnit)) {
            currentUnits.remove(currentUnit);
            return;
        }

        if (isShootingUnit(battleState, currentUnit)) {
            if (didShootAtTargetInRange(model, battleState, currentUnit)) {
                currentUnits.remove(currentUnit);
                return;
            }
        }

        List<List<MyPair<Integer, BattleDirection>>> distanceGrid = makeDistanceGrid(currentUnit, battleState);
        printGrid(distanceGrid, currentUnit);

        List<BattleUnit> candidates = findDistances(currentUnit, battleState, distanceGrid);
        printRankings(candidates, distanceGrid, battleState);

        if (!candidates.isEmpty()) {
            List<BattleDirection> steps = findSteps(battleState, distanceGrid, candidates.get(0),
                    battleState.getPositionForUnit(currentUnit));
            printSteps(steps);
            executeStepsTowardTarget(model, battleState, steps, currentUnit, currentUnits);
        } else {
            System.out.println("No targets...");
            currentUnits.remove(currentUnit);
        }
    }

    private boolean didShootAtTargetInRange(Model model, BattleState battleState,
                                            BattleUnit currentUnit) {
        ShootBattleAction shootBattleAction = new SilentShootBattleAction(currentUnit, battleState);
        List<BattleUnit> unitsInRange = shootBattleAction.getUnitsInRange(battleState);
        unitsInRange.removeIf((BattleUnit bu) -> bu.getOrigin().equals(currentUnit.getOrigin()));
        if (unitsInRange.isEmpty()) {
            return false;
        }
        // Prefer closer units as they can pose threat.
        Point pos = battleState.getPositionForUnit(currentUnit);
        Collections.sort(unitsInRange, (b1, b2) -> {
            double dist1 = battleState.getPositionForUnit(b1).distance(pos)*1000 - b1.getCount();
            double dist2 = battleState.getPositionForUnit(b2).distance(pos)*1000 - b2.getCount();
            return (int)Math.round(dist1 - dist2);
        });
        System.out.println("Shooting candidates:");
        for (BattleUnit b : unitsInRange) {
            System.out.println(b.getQualifiedName());
        }
        BattleUnit best = unitsInRange.get(0);
        shootBattleAction.setSelectedPoint(battleState.getPositionForUnit(best));
        shootBattleAction.execute(model, battleState, currentUnit);
        return true;
    }

    private boolean isShootingUnit(BattleState state, BattleUnit currentUnit) {
        return MyLists.any(currentUnit.getBattleActions(state), (BattleAction ba) -> ba instanceof ShootBattleAction);
    }

    private void executeStepsTowardTarget(Model model, BattleState battleState, List<BattleDirection> steps,
                                          BattleUnit currentUnit, ArrayList<BattleUnit> currentUnits) {
        MoveOrAttackBattleAction battleAction = new SilentMoveOrAttackBattleAction(currentUnit);
        battleAction.setParameters(model, battleState, steps.get(steps.size()-1));
        if (!battleAction.isValid()) { // not enough MP
            System.out.println("Battle action was not valid...");
            if (triesMap.get(currentUnit) == null) {
                System.out.println("... first try. Putting unit last in queue.");
                triesMap.put(currentUnit, 1);
                cycle(currentUnits, currentUnit);
            } else if (triesMap.get(currentUnit) == 3) {
                System.out.println("... third try, done with unit.");
                currentUnits.remove(currentUnit);
            } else {
                int tryCount = triesMap.get(currentUnit) + 1;
                System.out.println("... " + MyStrings.numberWord(tryCount) + " try. Putting unit last in queue.");
                triesMap.put(currentUnit, tryCount);
                cycle(currentUnits, currentUnit);
            }
        } else {
            battleAction.execute(model, battleState, currentUnit);
        }
    }

    private void cycle(ArrayList<BattleUnit> currentUnits, BattleUnit currentUnit) {
        currentUnits.remove(currentUnit);
        currentUnits.add(currentUnit);
    }

    private List<BattleDirection> findSteps(BattleState battleState,
                                            List<List<MyPair<Integer, BattleDirection>>> distanceGrid, BattleUnit target,
                                            Point endPosition) {
        List<BattleDirection> path = new ArrayList<>();
        Point pos = battleState.getPositionForUnit(target);
        path.add(distanceGrid.get(pos.x).get(pos.y).second);
        while (true) {
            int least = distanceGrid.get(pos.x).get(pos.y).first;
            Point leastPoint = null;
            BattleDirection leastDir = null;
            for (BattleDirection dir : DIRECTION_LIST) {
                Point next = new Point(pos.x + dir.dxdy.x, pos.y + dir.dxdy.y);
                if (isWithinGrid(next, distanceGrid)) {
                    if (distanceGrid.get(next.x).get(next.y).first < least) {
                        leastDir = distanceGrid.get(next.x).get(next.y).second;
                        leastPoint = next;
                        least = distanceGrid.get(next.x).get(next.y).first;
                    }
                }
            }
            pos = leastPoint;
            if (pos.equals(endPosition)) {
                break;
            }
            path.add(leastDir);
        }
        return path;
    }

    private List<List<MyPair<Integer, BattleDirection>>> makeDistanceGrid(BattleUnit currentUnit, BattleState battleState) {
        List<List<MyPair<Integer, BattleDirection>>> grid =
                new ArrayList<>();
        for (int x = 0; x < BattleState.BATTLE_GRID_WIDTH; ++x) {
            grid.add(new ArrayList<>());
            for (int y = 0; y < BattleState.BATTLE_GRID_HEIGHT; ++y) {
                grid.get(x).add(null);
            }
        }
        Point origin = battleState.getPositionForUnit(currentUnit);
        grid.get(origin.x).set(origin.y, new MyPair<>(0, currentUnit.getDirection()));
        List<MyPair<Point, BattleDirection>> queue = new ArrayList<>();
        queue.add(new MyPair<>(origin, currentUnit.getDirection()));

        while (!queue.isEmpty()) {
            MyPair<Point, BattleDirection> currentPair = queue.remove(0);
            Point current = currentPair.first;
            BattleDirection direction = currentPair.second;

            for (BattleDirection dir : DIRECTION_LIST) {
                Point newPos = new Point(current.x + dir.dxdy.x, current.y + dir.dxdy.y);
                if (!isWithinGrid(newPos, grid)) {
                    continue;
                }

                int terrainCost = calcTerrainCost(battleState, newPos, currentUnit);
                int turnCost    = dir == direction ? 0 : (dir.isOpposite(direction) ? 2 : 1);
                MyPair<Integer, BattleDirection> gridElem = grid.get(newPos.x).get(newPos.y);
                int sum = grid.get(current.x).get(current.y).first + terrainCost + turnCost;
                if (gridElem == null || sum <= gridElem.first) {
                    grid.get(newPos.x).set(newPos.y, new MyPair<>(sum, dir));
                    queue.add(new MyPair<>(newPos, dir));
                }
            }
        }
        return grid;
    }

    private boolean isWithinGrid(Point newPos, List<List<MyPair<Integer, BattleDirection>>> grid) {
        return !(newPos.x < 0 || newPos.x >= grid.size() || newPos.y < 0 || newPos.y >= grid.get(0).size());
    }

    private int calcTerrainCost(BattleState battleState, Point current, BattleUnit currentUnit) {
        BattleTerrain terrain = battleState.getTerrainForPosition(current);
        int terrainCost = 2;
        if (terrain != null) {
            if (terrain.getMoveCost() == BattleTerrain.IMPASSIBLE_TERRAIN_MOVE_COST) {
                terrainCost = IMPASSIBLE_TERRAIN_COST;
            } else {
                terrainCost = terrain.getMoveCost();
            }
        }
        BattleUnit other = battleState.getUnitForPosition(current);
        if (other != null && other.getOrigin().equals(currentUnit.getOrigin())) {
            terrainCost = 99;
        }
        return terrainCost;
    }

    private List<BattleUnit> findDistances(BattleUnit currentUnit, BattleState battleState,
                                           List<List<MyPair<Integer, BattleDirection>>> grid) {
        List<BattleUnit> result = new ArrayList<>(battleState.getOpposingUnits(currentUnit));
        result.removeIf((BattleUnit bu) -> bu.getCount() == 0);
        result.sort((b1, b2) -> {
            Point pos1 = battleState.getPositionForUnit(b1);
            Point pos2 = battleState.getPositionForUnit(b2);
            MyPair<Integer, BattleDirection> elem1 = grid.get(pos1.x).get(pos1.y);
            MyPair<Integer, BattleDirection> elem2 = grid.get(pos2.x).get(pos2.y);
            int val1 = elem1.first * 10000 +
                    flankBonus(b1.getDirection(), elem1.second) * 100 + b1.getCount();
            int val2 = elem2.first * 10000 +
                    flankBonus(b2.getDirection(), elem2.second) * 100 + b2.getCount();
            return val1 - val2;
        });
        return result;
    }

    private int flankBonus(BattleDirection direction, BattleDirection second) {
        if (direction == second) {
            return 2;
        }
        if (direction.isOpposite(second)) {
            return 0;
        }
        return 1;
    }


    private void printGrid(List<List<MyPair<Integer, BattleDirection>>> distanceGrid, BattleUnit currentUnit) {
        System.out.println("Distances for " + currentUnit.getQualifiedName());
        for (int y = 0; y < distanceGrid.get(0).size(); ++y) {
            for (int x = 0; x < distanceGrid.size(); ++x) {
                MyPair<Integer, BattleDirection> gridElem = distanceGrid.get(x).get(y);
                if (gridElem == null) {
                    System.out.print("   - ");
                } else if (gridElem.first >= IMPASSIBLE_TERRAIN_COST) {
                    System.out.print(" III ");
                } else {
                    System.out.printf("%4d", gridElem.first);
                    System.out.print(directionToString(gridElem.second));

                }
            }
            System.out.println("");
        }
    }

    private String directionToString(BattleDirection dir) {
        switch (dir) {
            case east:
                return ">";
            case west:
                return "<";
            case north:
                return "^";
            case south:
                return "v";
        }
        return "Error";
    }

    private void printSteps(List<BattleDirection> steps) {
        for (int i = steps.size()-1; i >= 0; --i) {
            System.out.println(directionToString(steps.get(i)));
        }
    }

    private void printRankings(List<BattleUnit> candidates, List<List<MyPair<Integer, BattleDirection>>> distanceGrid,
                               BattleState battleState) {
        System.out.println("Ranking: ");
        for (BattleUnit bu : candidates) {
            Point pos1 = battleState.getPositionForUnit(bu);
            MyPair<Integer, BattleDirection> elem = distanceGrid.get(pos1.x).get(pos1.y);
            int flankBonus = flankBonus(elem.second, bu.getDirection());
            System.out.println(bu.getQualifiedName() + ", MP Cost: " + elem.first + ", " +
                    (flankBonus == 2 ? "REAR ATTACK" : (flankBonus == 1) ? "Flank attack" : ""));
        }
    }
}
