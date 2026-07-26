package model.states.events;

import model.Model;
import model.map.Direction;
import model.map.MountainHex;
import model.states.DailyEventState;
import util.Arithmetics;
import view.subviews.SubView;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MountainRoadsEvent extends DailyEventState {
    private int dirIndex;
    private MountainHex hex;
    private Map<Integer, Boolean> dirs;
    private List<Integer> ALL_DIRS = List.of(Direction.SOUTH, Direction.SOUTH_WEST, Direction.NORTH_WEST,
            Direction.NORTH, Direction.NORTH_EAST, Direction.SOUTH_EAST);

    public MountainRoadsEvent(Model m) {
        super(m);
        dirs = new HashMap<>();
        for (Integer i : ALL_DIRS) {
            dirs.put(i, false);
        }
        this.dirIndex = 0;
        this.hex = new MountainHex(0, 0, 0);
    }

    @Override
    protected void doEvent(Model model) {
        model.setSubView(new ShowMountainSubView());

        do {
            waitForReturnSilently();
            dirs.put(ALL_DIRS.get(dirIndex), !dirs.get(ALL_DIRS.get(dirIndex)));
            int roads = 0;
            for (Integer i : ALL_DIRS) {
                if (dirs.get(i)) {
                    roads = roads | i;
                }
            }
            this.hex = new MountainHex(roads, 0, 0);
        } while (true);
    }

    private class ShowMountainSubView extends SubView {
        @Override
        protected void drawArea(Model model) {
            hex.drawYourself(model.getScreenHandler(), X_OFFSET + 2, Y_OFFSET + 2, 0);
        }

        @Override
        protected String getUnderText(Model model) {
            return "Current dir: " + Direction.getLongNameForDirection(ALL_DIRS.get(dirIndex)) + ", " +
                    dirs.get(ALL_DIRS.get(dirIndex));
        }

        @Override
        protected String getTitleText(Model model) {
            return "";
        }

        @Override
        public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
            if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                dirIndex = Arithmetics.decrementWithWrap(dirIndex, ALL_DIRS.size());
                return true;
            }
            if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                dirIndex = Arithmetics.incrementWithWrap(dirIndex, ALL_DIRS.size());
                return true;
            }
            return false;
        }
    }
}
