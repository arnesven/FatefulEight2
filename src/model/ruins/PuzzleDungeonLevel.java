package model.ruins;

import model.Model;
import model.Party;
import model.characters.GameCharacter;
import model.classes.Skill;
import model.ruins.factories.MonsterFactory;
import model.ruins.objects.*;
import model.ruins.themes.DungeonTheme;
import model.states.ExploreRuinsState;
import util.MyLists;
import util.MyPair;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.List;

public class PuzzleDungeonLevel extends DungeonLevel {

    private static final int NO_OF_LEVERS = 4;
    private static final int PLATES_ROWS = 4;
    private static final int PLATES_COLUMNS = 5;
    private final List<LeverObject> autoLevers = new ArrayList<>();
    private final Point exitPosition = new Point(4, 7);

    public PuzzleDungeonLevel(Model model, Random random, boolean firstLevel, boolean descending, DungeonTheme theme) {
        super(model, random, false, 3, theme, new MonsterFactory());
        PuzzleRoom puzzleRoom = new PuzzleRoom();
        setRoom(0, 2, puzzleRoom);
        setStartingPoint(new Point(0, 2));
        setDescentPoint(new Point(0, 2));
        DungeonDoor dobj = null;
        if (firstLevel) {
            dobj = new DungeonExit(exitPosition);
        } else if (descending) {
            dobj = new StairsUp(exitPosition);
        } else {
            dobj = new StairsDown(exitPosition);
        }
        getRoom(getStartingPoint()).setConnection(2, dobj);

        DungeonRoom descentRoom = new DungeonRoom();
        setRoom(0, 1, descentRoom);
        connectDoor(0, 2, "north", DungeonRoomConnection.OPEN);
        OpenDoor open = (OpenDoor) getRoom(getStartingPoint()).getConnection(0);
        open.setBlocked(true);

        if (descending) {
            descentRoom.setConnection(0, new StairsDown(new Point(1, 0)));
        } else {
            descentRoom.setConnection(0, new StairsUp(new Point(1, 0)));
        }

        for (int i = 0; i < NO_OF_LEVERS; ++i) {
            LeverObject lever = new AutomaticLever(new Point(i+2, 1));
            puzzleRoom.addObject(lever);
            open.addLeverConnection(lever);
            this.autoLevers.add(lever);
        }

        PressurePlateObject[][] plates = new PressurePlateObject[PLATES_COLUMNS][PLATES_ROWS];
        for (int i = 0; i < PLATES_ROWS; ++i) {
            for (int j = 0; j < PLATES_COLUMNS; ++j) {
                PressurePlateObject plate = new PressurePlateObject(j, i, autoLevers, puzzleRoom);
                puzzleRoom.addObject(plate);
                plates[j][i] = plate;
            }
        }
        applyReverseSolution(plates);
    }

    public void moveAvatarAwayFromStairs(ExploreRuinsState state) {
        Point mid = new Point((exitPosition.x-1)*4,(exitPosition.y-1)*4);
        state.generalMoveAvatar(new Point((exitPosition.x-1)*4, exitPosition.y*4), mid);
        Point relPos = state.getCurrentRoom().getRelativeAvatarPosition();
        state.generalMoveAvatar(mid, new Point(relPos.x*4, relPos.y*4));
    }

    private void applyReverseSolution(PressurePlateObject[][] plates) {
        List<Point> dxdys = List.of(new Point(1, 0), new Point(-1, 0),
                new Point(0, 1), new Point(0, -1));

        Point currentPos = new Point(MyRandom.randInt(PLATES_COLUMNS), 0);
        while (currentPos.y != PLATES_ROWS) {
            plates[currentPos.x][currentPos.y].toggle(false);
            plates[currentPos.x][currentPos.y].mark();
            Point dxdy = MyRandom.sample(dxdys);
            Point nextPos = new Point(currentPos.x + dxdy.x, currentPos.y + dxdy.y);
            if (0 <= nextPos.x && nextPos.x < PLATES_COLUMNS && 0 <= nextPos.y) {
                currentPos = nextPos;
            }
        }
    }

    @Override
    protected boolean buildRandomLevel(Model model, boolean firstLevel) { return true; }

    @Override
    public boolean showExitIcon() {
        return false;
    }

    @Override
    public boolean showMapIcon() {
        return false;
    }

    private static class PuzzleRoom extends DungeonRoom {
        private Point relPos;
        private boolean showMarkedPlates = false;

        public PuzzleRoom() {
            super(5, 6);
            this.relPos = new Point(3, 5);
        }

        @Override
        public void entryTrigger(Model model, ExploreRuinsState exploreRuinsState) {
            super.entryTrigger(model, exploreRuinsState);
            exploreRuinsState.leaderSay("Hmm... let's see now.");
            if (!showMarkedPlates) {
                if (model.getParty().size() > 1) {
                    exploreRuinsState.leaderSay("Anybody got a clue how this thing works?");
                }
                MyPair<Boolean, GameCharacter> pair = model.getParty().doSoloSkillCheckWithPerformer(model, exploreRuinsState, Skill.Logic, 10);
                boolean info = false;
                if (pair.first) {
                    model.getParty().partyMemberSay(model, pair.second, "Isn't it obvious? We need all those levers to be in the right position. " +
                            "The pressure plates probably control them. Although the creator of this puzzle has surely tried " +
                            "hiding which plate goes to what lever. And, come to think of it, perhaps some plates are connected to multiple levers.");
                    info = true;
                }

                pair = model.getParty().doSoloSkillCheckWithPerformer(model, exploreRuinsState, Skill.Perception, 12);
                if (pair.first) {
                    model.getParty().partyMemberSay(model, pair.second,
                            "Some of the pressure plates look a little different actually, like they're a little extra worn.");
                    this.showMarkedPlates = true;
                }

                if (!info && !showMarkedPlates) {
                    exploreRuinsState.leaderSay("I guess we'll just have to rely on brute force.");
                }
            }
        }

        public boolean isShowMarkedPlates() {
            return showMarkedPlates;
        }

        @Override
        public Point getRelativeAvatarPosition() {
            return relPos;
        }

        public void setAvatarPosition(Point internalPosition) {
            this.relPos = internalPosition;
        }

        public void setDoorBlocked(boolean b) {
            OpenDoor open = (OpenDoor) getConnection(0);
            open.setBlocked(b);
        }
    }

    private static class AutomaticLever extends LeverObject {
        private static final Sprite LEVER_ON = new Sprite32x32("onlever", "dungeon.png", 0x43,
                MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.DARK_BLUE, MyColors.PINK);
        private static final Sprite LEVER_OFF = new Sprite32x32("offlever", "dungeon.png", 0x44,
                MyColors.BLACK, MyColors.GRAY, MyColors.DARK_BLUE, MyColors.PINK);

        public AutomaticLever(Point point) {
            super(new Random(1));
            setInternalPosition(point);
            setOn(true);
        }

        @Override
        public void doAction(Model model, ExploreRuinsState state) {
            state.println("This lever is locked into place. How can it be moved?");
        }

        @Override
        protected Sprite getSprite(DungeonTheme theme) {
            if (isOn()) {
                return LEVER_ON;
            }
            return LEVER_OFF;
        }
    }

    private static final Sprite PLATE_OFF = new Sprite32x32("plateoff", "dungeon.png", 0x104,
            MyColors.GRAY, MyColors.DARK_GRAY, MyColors.DARK_BLUE, MyColors.PINK);
    private static final Sprite PLATE_ON = new Sprite32x32("plateon", "dungeon.png", 0x104,
            MyColors.DARK_GRAY, MyColors.GRAY, MyColors.DARK_BLUE, MyColors.PINK);
    private static final Sprite MARK = new Sprite32x32("platemark", "dungeon.png", 0x105,
            MyColors.RED, MyColors.GRAY, MyColors.DARK_BLUE, MyColors.PINK);

    private static class PressurePlateObject extends DungeonObject {
        private final PuzzleRoom puzzleRoom;
        private final int column;
        private final int row;
        private boolean isOn;
        private final List<LeverObject> autoLevers;
        private final List<LeverObject> allLevers;
        private boolean marked = false;

        public PressurePlateObject(int column, int row, List<LeverObject> autoLevers, PuzzleRoom puzzleRoom) {
            super(column+1, row+2);
            this.isOn = false;
            this.column = column;
            this.row = row;
            allLevers = autoLevers;
            autoLevers = new ArrayList<>(autoLevers);
            Collections.shuffle(autoLevers);
            for (int i = MyRandom.randInt(1, autoLevers.size()); i > 0; --i) {
                autoLevers.remove(0);
            }
            this.autoLevers = autoLevers;
            this.puzzleRoom = puzzleRoom;
        }

        @Override
        protected Sprite getSprite(DungeonTheme theme) {
            if (isOn) {
                return PLATE_ON;
            }
            return PLATE_OFF;
        }

        @Override
        public void drawYourself(Model model, int xPos, int yPos, DungeonTheme theme) {
            model.getScreenHandler().register(getSprite(theme).getName(), new Point(xPos, yPos), getSprite(theme));
            if (marked && puzzleRoom.isShowMarkedPlates()) {
                model.getScreenHandler().register(MARK.getName(), new Point(xPos, yPos), MARK, 1);
            }
        }

        @Override
        public String getDescription() {
            return "A pressure plate";
        }

        @Override
        public void doAction(Model model, ExploreRuinsState state) {
            int lastX = puzzleRoom.getRelativeAvatarPosition().x;
            int lastY = puzzleRoom.getRelativeAvatarPosition().y;
            Point newPos = new Point(column, row+1);

            if (Math.abs(lastX - newPos.x) + Math.abs(lastY - newPos.y) == 1 || lastY == 5) {
                Point fromPos = new Point(lastX * 4, lastY * 4);
                Point toPos = new Point((getInternalPosition().x - 1) * 4, (getInternalPosition().y - 1) * 4);
                state.generalMoveAvatar(fromPos, toPos);
                puzzleRoom.setAvatarPosition(newPos);
                toggle(true);
                if (MyLists.all(allLevers, LeverObject::isOn) && row == 0) {
                    state.generalMoveAvatar(toPos, new Point(0, 0));
                    puzzleRoom.setAvatarPosition(new Point(0, 0));
                    puzzleRoom.setDoorBlocked(false);
                } else {
                    puzzleRoom.setDoorBlocked(true);
                }
            } else {
                state.println("Please select an adjacent pressure plate.");
            }
        }

        public void toggle(boolean visibleChange) {
            if (visibleChange) {
                this.isOn = !isOn;
            }
            for (LeverObject lobj : autoLevers) {
                lobj.setOn(!lobj.isOn());
            }
        }

        public void mark() {
            this.marked = true;
        }
    }
}
