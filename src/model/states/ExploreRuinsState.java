package model.states;

import model.Model;
import model.SteppingMatrix;
import model.TimeOfDay;
import model.characters.GameCharacter;
import model.ruins.*;
import model.ruins.objects.*;
import util.MyLists;
import util.MyPair;
import view.combat.CaveTheme;
import view.combat.CombatTheme;
import view.combat.DungeonTheme;
import view.subviews.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ExploreRuinsState extends GameState {

    private final String dungeonType;
    private final String dungeonName;
    private RuinsDungeon dungeon;
    private int currentLevel = 0;
    private Point partyPosition;
    private SteppingMatrix<DungeonObject> matrix = new SteppingMatrix<>(8, 8);
    private RuinsSubView subView;
    private boolean dungeonExited = false;
    private boolean mapView = false;
    private List<DungeonMonster> defeatedMonsters = new ArrayList<>();
    private Set<DungeonRoom> visitedRooms = new HashSet<>();
    private Set<DungeonLevel> visitedLevels = new HashSet<>();
    private Set<Integer> mapsFound = new HashSet<>();

    public ExploreRuinsState(Model model, String ruinsName, String dungeonType) {
        super(model);
        this.dungeonName = ruinsName;
        dungeon = model.getDungeon(ruinsName, dungeonType.equals("Ruins"));
        currentLevel = 0;
        partyPosition = dungeon.getLevel(currentLevel).getStartingPoint();
        dungeon.getLevel(currentLevel).getRoom(partyPosition).setRevealedOnMap(true);
        this.dungeonType = dungeonType;
    }

    public ExploreRuinsState(Model model, RuinsDungeon dungeon, String dungeonType) {
        super(model);
        this.dungeonName = "Unknown";
        this.dungeon = dungeon;
        currentLevel = 0;
        partyPosition = dungeon.getLevel(currentLevel).getStartingPoint();
        DungeonRoom firstRoom = dungeon.getLevel(currentLevel).getRoom(partyPosition);
        firstRoom.setRevealedOnMap(true);
        visitRoom(firstRoom);
        this.dungeonType = dungeonType;
    }

    @Override
    public GameState run(Model model) {
        model.getTutorial().dungeons(model);
        doStuff(model);
        if (model.getParty().isWipedOut()) {
            return new GameOverState(model);
        }
        model.setTimeOfDay(TimeOfDay.EVENING);
        return model.getCurrentHex().getEveningState(model, false, false);
    }

    private void doStuff(Model model) {
        populateMatrix();
        subView = new RuinsSubView(this, matrix, dungeonType);
        CollapsingTransition.transition(model, subView);
        getCurrentRoom().entryTrigger(model, this);
        do {
            waitForReturnSilently();
            DungeonObject selected = matrix.getSelectedElement();
            selected.doAction(model, this);
            populateMatrix();
            if (matrix.getElementList().contains(selected)) {
                matrix.setSelectedElement(selected);
            } else if (selected instanceof DungeonDoor) {
                setOtherDoorAsSelected(selected);
            }
        } while (!dungeonExited);
        model.setSubView(new DungeonStatsSubView(dungeon, dungeonType, visitedRooms.size(),
                visitedLevels.size(), defeatedMonsters.size(), mapsFound.size()));
        print("Press enter to continue.");
        waitForReturn();
    }

    private void setOtherDoorAsSelected(DungeonObject selected) {
        List<DungeonDoor> doors = MyLists.filter(MyLists.transform(MyLists.filter(matrix.getElementList(),
                dobj -> dobj instanceof DungeonDoor),
                dobj -> (DungeonDoor) dobj),
                 door -> door.getLinkedDoor() != selected);
        if (doors.isEmpty()) {
            return;
        }
        doors.sort((o1, o2) -> {
            int score1 = o1 instanceof OpenDoor ? 0 : 1;
            int score2 = o2 instanceof OpenDoor ? 0 : 1;
            return score1 - score2;
        });
        matrix.setSelectedElement(doors.getFirst());
    }

    private void populateMatrix() {
        matrix.clear();
        List<MyPair<DungeonObject, Point>> objects = dungeon.getObjectsAndPositions(partyPosition, currentLevel);
        for (MyPair<DungeonObject, Point> pair : objects) {
            matrix.addElement(pair.second.x, pair.second.y, pair.first);
        }
        if (dungeon.getLevel(getCurrentLevel()).showMapIcon()) {
            matrix.addElement(2, 7, new DungeonMapIcon());
        }
        if (dungeon.getLevel(getCurrentLevel()).showExitIcon()) {
            matrix.addElement(3, 7, new ExitDungeonIcon());
        }
    }

    public RuinsDungeon getDungeon() {
        return dungeon;
    }

    public Point getPartyPosition() {
        return partyPosition;
    }

    public void goDirection(Model model, Point dir) {
        Point nextPosition = new Point(partyPosition);
        nextPosition.x += dir.x;
        nextPosition.y += dir.y;
        dungeon.setAvatarEnabled(false);
        dungeon.setCursorEnabled(false);
        if (nextPosition.x / 2 == partyPosition.x / 2 && nextPosition.y / 2 == partyPosition.y / 2) {
            subView.addMovementAnimation(model.getParty().getLeader().getAvatarSprite(),
                    RuinsDungeon.avatarPosition(partyPosition),
                    RuinsDungeon.avatarPosition(nextPosition));
            subView.waitForAnimation();
            subView.removeMovementAnimation();
        } else {
            Point conv = moveToEdge(RuinsDungeon.avatarPosition(partyPosition), dir);
            subView.addMovementAnimation(model.getParty().getLeader().getAvatarSprite(),
                    RuinsDungeon.avatarPosition(partyPosition),
                    conv);
            subView.waitForAnimation();
            subView.removeMovementAnimation();
            partyPosition = nextPosition;
            populateMatrix();
            conv = moveToEdge(RuinsDungeon.avatarPosition(nextPosition), new Point(-dir.x, -dir.y));
            subView.addMovementAnimation(model.getParty().getLeader().getAvatarSprite(),
                    conv, RuinsDungeon.avatarPosition(nextPosition));
            subView.waitForAnimation();
            subView.removeMovementAnimation();

        }
        dungeon.setAvatarEnabled(true);
        dungeon.setCursorEnabled(true);
        partyPosition = nextPosition;
        populateMatrix();
        getCurrentRoom().entryTrigger(model, this);
    }

    private Point moveToEdge(Point conv, Point dir) {
        if (dir.x > 0) {
            conv.x += dir.x * 4 * 3;
        } else {
            conv.x += dir.x * 4 * 2;
        }

        if (dir.y > 0) {
            conv.y += dir.y * 4 * 3;
        } else {
            conv.y += dir.y * 4 * 2;
        }
        return conv;
    }

    public void unlockDoor(Model model, String direction) {
        dungeon.getLevel(currentLevel).connectDoor(partyPosition.x, partyPosition.y, direction, DungeonRoomConnection.OPEN);
    }

    public void setDungeonExited(boolean b) {
        this.dungeonExited = b;
    }

    public DungeonRoom getCurrentRoom() {
        return dungeon.getLevel(currentLevel).getRoom(partyPosition);
    }

    public SubView getSubView() {
        return subView;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void ascendOrDescend(boolean downWard) {
        dungeon.getLevel(currentLevel).moveAvatarTowardStairs(this);
        currentLevel += downWard ? 1 : -1;
        changeLevel(downWard);
        dungeon.getLevel(currentLevel).moveAvatarAwayFromStairs(this);
        getCurrentRoom().entryTrigger(getModel(), this);
    }

    public void descend() {
        ascendOrDescend(true);
    }

    public void ascend() {
        ascendOrDescend(false);
    }

    private void generalMoveAnimation(int fromX, int fromY, int toX, int toY, GameCharacter who) {
        dungeon.setAvatarEnabled(false);
        dungeon.setCursorEnabled(false);
        Point position = RuinsDungeon.avatarPosition(partyPosition);
        subView.addMovementAnimation(who.getAvatarSprite(),
                new Point(position.x+fromX, position.y+fromY),
                new Point(position.x+toX, position.y+toY));
        subView.waitForAnimation();
        subView.removeMovementAnimation();
        dungeon.setAvatarEnabled(true);
        dungeon.setCursorEnabled(true);
    }

    private void generalMoveAnimation(int fromX, int fromY, int toX, int toY) {
        generalMoveAnimation(fromX, fromY, toX, toY, getModel().getParty().getLeader());
    }

    private void changeLevel(boolean down) {
        if (down) {
            partyPosition = dungeon.getLevel(currentLevel).getStartingPoint();
        } else {
            partyPosition = dungeon.getLevel(currentLevel).getDescentPoint();
        }
        populateMatrix();
        DungeonRoom[][] roomsMatrix = dungeon.getLevel(currentLevel).getRooms();
        for (int x = 0; x < roomsMatrix.length; ++x) {
            for (int y = 0; y < roomsMatrix[0].length; ++y) {
                if (roomsMatrix[x][y] != null) {
                    for (DungeonObject dobj : roomsMatrix[x][y].getObjects()) {
                        if (dobj instanceof StairsDown) {
                            System.out.println("Stairs down at x=" + x + ", y=" + y);
                        }
                        if (dobj instanceof StairsUp) {
                            System.out.println("Stairs up at x=" + x + ", y=" + y);
                        }
                    }
                }
            }
        }
    }

    public void moveCharacterToCenterAnimation(Model model, Point where) {
        generalMoveAnimation(0, 0, where.x*4, where.y*4);
    }

    public void generalMoveAvatar(Point from, Point to) {
        generalMoveAnimation(from.x, from.y, to.x, to.y);
    }

    public boolean isDungeonExited() {
        return dungeonExited;
    }

    public void setMapView(boolean b) {
        dungeon.setAvatarEnabled(!b);
        dungeon.setCursorEnabled(!b);
        this.mapView = b;
    }

    public boolean isMapView() {
        return this.mapView;
    }

    public CombatTheme getCombatTheme() {
        CombatTheme theme = new CaveTheme();
        if (getCurrentLevel() < DungeonMaker.NUMBER_OF_UPPER_LEVELS) {
            theme = new DungeonTheme();
        }
        return theme;
    }

    protected void setCurrentLevelAndPosition(int level, Point position) {
        currentLevel = level;
        this.partyPosition = position;
    }

    public String getCurrentRoomInfo() {
        return "Level " + (getCurrentLevel()+1) + ", Room " + (getPartyPosition().x+1) + "-" + (getPartyPosition().y+1);
    }

    public void movePartyToRoom(Model model, int level, Point p) {
        this.currentLevel = level;
        this.partyPosition = p;
        populateMatrix();
    }

    public String getDungeonType() {
        return dungeonType;
    }

    public void addDefeatedMonster(DungeonMonster dungeonMonster) {
        defeatedMonsters.add(dungeonMonster);
    }

    public void visitRoom(DungeonRoom dungeonRoom) {
        visitedRooms.add(dungeonRoom);
        for (int i = 0; i < dungeon.getNumberOfLevels(); ++i) {
            if (dungeon.getLevel(i).getRoomList().contains(dungeonRoom)) {
                visitedLevels.add(dungeon.getLevel(i));
                break;
            }
        }
    }

    public void mapsFound(int currentLevel) {
        this.mapsFound.add(currentLevel);
    }

    public void completeAchievement() {
        super.completeAchievement(dungeonName);
    }
}
