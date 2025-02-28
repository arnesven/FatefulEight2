package model.states.events;

import model.Model;
import model.map.SeaHex;
import model.map.World;
import model.states.DailyEventState;
import model.states.TravelBySeaState;
import view.sprites.Sprite;
import view.subviews.CollapsingTransition;
import view.subviews.EmptySubView;
import view.subviews.MapSubView;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public abstract class AlternativeTravelEvent extends DailyEventState {

    private final boolean isWaterTravel;

    public AlternativeTravelEvent(Model model, boolean isWaterTravel) {
        super(model);
        this.isWaterTravel = isWaterTravel;
    }

    protected abstract Sprite getSprite();
    protected abstract boolean eventIntro(Model model);
    protected abstract void eventOutro(Model model);
    protected abstract String getTitleText();
    protected abstract String getUnderText();
    protected abstract String getTravelPrompt();
    protected abstract boolean isSelectableDestination(Point startPoint, Point cursorPos, int dx, int dy);

    @Override
    protected final void doEvent(Model model) {
        if (!eventIntro(model)) {
            return;
        }
        Sprite sprite = getSprite();
        model.getWorld().setAlternativeAvatar(sprite);
        ExplicitTravelSubView mapSubView = new ExplicitTravelSubView(model);
        CollapsingTransition.transition(model, mapSubView);
        Point selectedPos = selectDirection(model, mapSubView);
        model.getWorld().setAlternativeAvatar(null);
        mapSubView.setAvatarEnabled(false);
        if (isWaterTravel) {
            TravelBySeaState.travelBySea(model, model.getWorld().getHex(selectedPos),
                    this, sprite, false, false);
        } else {
            mapSubView.addMovementAnimation(
                    sprite,
                    model.getWorld().translateToScreen(model.getParty().getPosition(), model.getParty().getPosition(), MapSubView.MAP_WIDTH_HEXES, MapSubView.MAP_HEIGHT_HEXES),
                    model.getWorld().translateToScreen(selectedPos, model.getParty().getPosition(), MapSubView.MAP_WIDTH_HEXES, MapSubView.MAP_HEIGHT_HEXES));
            mapSubView.waitForAnimation();
        }
        CollapsingTransition.transition(model, new EmptySubView());
        model.getCurrentHex().travelFrom(model);
        model.getParty().setPosition(selectedPos);
        model.getParty().setOnRoad(false);
        setCurrentTerrainSubview(model);
        model.getCurrentHex().travelTo(model);

        eventOutro(model);
    }

    protected boolean isValidDestination(Model model, Point selectedPos) {
        return model.getWorld().getHex(selectedPos).canTravelTo(model);
    }

    protected Point selectDirection(Model model, ExplicitTravelSubView mapSubView) {
        Point selectedDir;
        boolean valid;
        do {
            valid = true;
            print(getTravelPrompt());
            waitForReturn();
            selectedDir = mapSubView.getCurrentPosition(model);
            boolean seaHex = model.getWorld().getHex(selectedDir) instanceof SeaHex;
            if (!isValidDestination(model, selectedDir) || (seaHex && dontAllowSeaHexes())) {
                println("That is not a valid destination.");
                valid = false;
            }
        } while ((selectedDir.x == model.getParty().getPosition().x &&
                selectedDir.y == model.getParty().getPosition().y) || !valid);
        return selectedDir;
    }

    protected boolean dontAllowSeaHexes() {
        return true;
    }

    private class ExplicitTravelSubView extends MapSubView {
        private final Point cursorPos;
        private final Point startPoint;
        private final String titleText;
        private final String underText;
        private boolean enabled = true;

        public ExplicitTravelSubView(Model model) {
            super(model);
            cursorPos = new Point(model.getParty().getPosition());
            startPoint = new Point(model.getParty().getPosition());
            this.titleText = AlternativeTravelEvent.this.getTitleText();
            this.underText = AlternativeTravelEvent.this.getUnderText();
        }

        @Override
        protected String getTitleText(Model model) {
            return titleText;
        }

        @Override
        protected String getUnderText(Model model) {
            return underText;
        }

        @Override
        public void specificDrawArea(Model model) {
            model.getWorld().drawYourself(model, model.getParty().getPosition(), model.getParty().getPosition(),
                    MAP_WIDTH_HEXES, MAP_HEIGHT_HEXES, Y_OFFSET, cursorPos, enabled);
        }

        @Override
        public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
            return handleMapMovement(keyEvent, model);
        }

        public boolean handleMapMovement(KeyEvent keyEvent, Model model) {
            if (keyEvent.getKeyCode() == KeyEvent.VK_DOWN) {
                moveCursor(0, 1);
                return true;
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
                moveCursor(1, 0);
                return true;
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
                moveCursor(-1, 0);
                return true;
            } else if (keyEvent.getKeyCode() == KeyEvent.VK_UP) {
                moveCursor(0, -1);
                return true;
            }
            return false;
        }

        private void moveCursor(int dx, int dy) {
            if (AlternativeTravelEvent.this.isSelectableDestination(startPoint, cursorPos, dx, dy)) {
                getModel().getWorld().move(cursorPos, dx, dy);
            }
        }

        public Point getCurrentPosition(Model model) {
            return cursorPos;
        }

        public void setAvatarEnabled(boolean b) {
            enabled = b;
        }
    }

}
