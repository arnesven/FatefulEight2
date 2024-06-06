package model.ruins.configs;

import model.ruins.DungeonLevel;
import model.ruins.DungeonRoom;
import model.ruins.factories.MonsterFactory;
import model.ruins.objects.*;
import model.ruins.themes.DungeonTheme;
import util.MyPair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class DungeonLevelConfig implements Serializable {

    private final double chests;
    private double levers;
    private final double corpses;
    private double monsters;
    private final double lockedDoors;
    private final double spikeTraps;
    private double pitfallTraps;
    private final double campFires;
    private DungeonTheme theme;
    private final MonsterFactory monsterFactory;
    private List<MyPair<DungeonObject, Double>> requiredObjects = new ArrayList<>();

    private static final double CHEST_PREVALENCE = 0.4;
    private static final double LEVER_PREVALENCE = 0.3;
    private static final double CORPSE_PREVALENCE = 0.3;
    private static final double MONSTER_PREVALENCE = 0.1;
    protected static final double LOCKED_DOOR_PREVALENCE = 0.05;
    private static final double SPIKE_TRAP_PREVALENCE = 0.1;
    private static final double PITFALL_TRAP_PREVALENCE = 0.0;
    private static final double CAMPFIRE_PREVALENCE = 0.0;

    public DungeonLevelConfig(DungeonTheme theme, MonsterFactory monsterFactory,
                              double chests, double levers, double corpses, double monsters,
                              double lockedDoors, double spikeTraps, double pitfallTraps, double campfires) {
        this.theme = theme;
        this.monsterFactory = monsterFactory;
        this.chests = chests;
        this.levers = levers;
        this.corpses = corpses;
        this.monsters = monsters;
        this.lockedDoors = lockedDoors;
        this.spikeTraps = spikeTraps;
        this.pitfallTraps = pitfallTraps;
        this.campFires = campfires;
    }

    public DungeonLevelConfig(DungeonTheme theme, MonsterFactory monsterFactory) {
        this(theme, monsterFactory, CHEST_PREVALENCE, LEVER_PREVALENCE, CORPSE_PREVALENCE, MONSTER_PREVALENCE,
                LOCKED_DOOR_PREVALENCE, SPIKE_TRAP_PREVALENCE, PITFALL_TRAP_PREVALENCE, CAMPFIRE_PREVALENCE);
    }

    public void addRequiredDeadEndObject(DungeonObject object, double prevalence) {
        requiredObjects.add(new MyPair<>(object, prevalence));
    }

    public boolean allRequiredObjectsPlaced() {
        return requiredObjects.isEmpty();
    }

    public DungeonTheme getTheme() {
        return theme;
    }

    public void setTheme(DungeonTheme theme) {
        this.theme = theme;
    }

    public double getLockedDoorPrevalence() {
        return lockedDoors;
    }

    protected void setMonsterPrevalence(double v) {
        this.monsters = v;
    }

    protected void setLeverPrevalence(double v) {
        this.levers = v;
    }

    protected void setPitfallTrapPrevalence(double v) {
        this.pitfallTraps = v;
    }

    public final void addContent(DungeonLevel dungeonLevel, Set<DungeonRoom> visitedRooms, Random random) {
        addDeadEndRoomObjects(dungeonLevel, visitedRooms, random);
        addJunctionRoomObjects(dungeonLevel, visitedRooms, random);
        addDecorations(dungeonLevel, visitedRooms, random, monsterFactory);
    }

    protected void addDecorations(DungeonLevel dungeonLevel, Set<DungeonRoom> visitedRooms,
                                  Random random, MonsterFactory monsterFactory) { }

    protected boolean junctionRoomHook(DungeonRoom room, Random random, MonsterFactory monsterFactory) { return true; }

    protected boolean deadEndRoomHook(DungeonRoom room, Random random, MonsterFactory monsterFactory) { return true; }

    private void addJunctionRoomObjects(DungeonLevel dungeonLevel, Set<DungeonRoom> visitedRooms, Random random) {
        DungeonRoom startingRoom = dungeonLevel.getRoom(dungeonLevel.getStartingPoint());
        DungeonRoom endingRoom = dungeonLevel.getRoom(dungeonLevel.getDescentPoint());
        for (DungeonRoom room : visitedRooms) {
            if (room.getCardinality() > 1) {
                if (startingRoom != room && endingRoom != room) {
                    if (junctionRoomHook(room, random, monsterFactory)) {
                        addJunctionObject(room, random, monsterFactory);
                    }
                    junctionRoomHook(room, random, monsterFactory);
                }
            }
        }
    }

    private void addDeadEndRoomObjects(DungeonLevel dungeonLevel, Set<DungeonRoom> visitedRooms, Random random) {
        for (DungeonRoom room : visitedRooms) {
            if (room.getCardinality() == 1) {
                if (deadEndRoomHook(room, random, monsterFactory)) {
                    addDeadEndObject(dungeonLevel, room, random);
                }
            }
        }
    }

    protected final void addJunctionObject(DungeonRoom room, Random random, MonsterFactory monsterFactory) {
        double roll = random.nextDouble();
        if (roll < monsters) {
            room.addObject(monsterFactory.makeRandomEnemies(random));
        } else if (roll < monsters + spikeTraps) {
            DungeonSpikeTrap.makeSpikeTrap(room, random);
        } else if (roll < monsters + spikeTraps + pitfallTraps) {
            DungeonPitfallTrap.makePitfallTrap(room, random);
        } else if (roll < monsters + spikeTraps + pitfallTraps + campFires) {
            room.addObject(new CampfireDungeonObject());
        }
    }

    protected final void addDeadEndObject(DungeonLevel dungeonLevel, DungeonRoom room, Random random) {
        double roll = random.nextDouble();
        for (MyPair<DungeonObject, Double> pair : new ArrayList<>(requiredObjects)) {
            if (roll < pair.second) {
                room.addObject(pair.first);
                requiredObjects.remove(pair);
                return;
            }
        }

        if (roll < chests) {
            room.addObject(new DungeonChest(random));
        } else if (roll < chests + levers) {
            LeverObject lever = new LeverObject(random);
            room.addObject(lever);
            dungeonLevel.getRoom(dungeonLevel.getDescentPoint()).connectLeverToDoor(lever);
        } else if (roll < chests + levers + corpses) {
            room.addObject(new CorpseObject());
        }
    }
}
