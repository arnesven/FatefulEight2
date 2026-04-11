package model.states.mine;

import model.Model;
import model.SteppingMatrix;
import model.ruins.objects.CorpseObject;
import util.MyRandom;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.subviews.DungeonDrawer;

import java.awt.*;

public class AbandonedMine extends LogicalMine {

    private static final MockDrawer mockDrawer = new MockDrawer();

    @Override
    public void addNPCs(SteppingMatrix<MineObject> matrix, int level) {
        if (level > 5 || MyRandom.randInt(3) != 0) {
            return;
        }
        for (int i = MyRandom.randInt(1, level + 1); i >= 0; --i) {
            placeRandomly(matrix, new CorpseMineObject());
        }
    }

    private static class CorpseMineObject extends MineObject {

        private final CorpseObject dungeonEquivalent = new CorpseObject();

        @Override
        public Point gotMovedInto(Model model, AdvancedMineEvent state, Point currentLocation) {
            dungeonEquivalent.doAction(model, state);
            return currentLocation;
        }

        @Override
        public void drawYourself(ScreenHandler screenHandler, LogicalMine mine, Point screenPosition) {
            mockDrawer.setScreenHandler(screenHandler);
            dungeonEquivalent.drawYourself(mockDrawer, screenPosition.x, screenPosition.y, null);
        }
    }

    private static class MockDrawer extends DungeonDrawer {
        private ScreenHandler screenHandler;

        public MockDrawer() {
            super(null);
        }

        @Override
        public void register(String s, Point p, Sprite sprite) {
            screenHandler.register(s, p, sprite);
        }

        public void setScreenHandler(ScreenHandler handler) {
            this.screenHandler = handler;
        }
    }
}
