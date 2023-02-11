package model.states.events;

import model.Model;
import model.map.World;
import model.states.DailyEventState;
import model.states.TravelState;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;
import view.subviews.CollapsingTransition;
import view.subviews.EmptySubView;
import view.subviews.MapSubView;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class PegasusEvent extends DailyEventState {
    private Sprite pegasusSprite = new PegasusSprite();

    public PegasusEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("A winged horse lands in front of the party. It seems to be " +
                "willing to give them a ride.");
        if (model.getParty().size() > 3) {
            model.getParty().randomPartyMemberSay(model, List.of("But there's too many of us to fit on it's back."));
            println("The pegasus shakes its head like it can understand you, then it flies away.");
            model.getParty().randomPartyMemberSay(model, List.of("What a shame..."));
            return;
        }
        model.getParty().randomPartyMemberSay(model,
                List.of("We're gonna fly? Oh what a wonderful sensation to soar among the clouds!"));
        print("Do you ride the pegasus? (Y/N) ");
        if (yesNoInput()) {
            model.getWorld().setAlternativeAvatar(pegasusSprite);
            PegasusTravelSubView mapSubView = new PegasusTravelSubView(model);
            CollapsingTransition.transition(model, mapSubView);
            Point selectedPos = selectDirection(model, mapSubView);
            model.getWorld().setAlternativeAvatar(null);
            mapSubView.setAvatarEnabled(false);
            mapSubView.addMovementAnimation(
                    pegasusSprite,
                    World.translateToScreen(model.getParty().getPosition(), model.getParty().getPosition(), MapSubView.MAP_WIDTH_HEXES, MapSubView.MAP_HEIGHT_HEXES),
                    World.translateToScreen(selectedPos, model.getParty().getPosition(), MapSubView.MAP_WIDTH_HEXES, MapSubView.MAP_HEIGHT_HEXES));
            mapSubView.waitForAnimation();

            CollapsingTransition.transition(model, new EmptySubView());
            model.getCurrentHex().travelFrom(model);
            model.getParty().setPosition(selectedPos);
            model.getParty().setOnRoad(false);
            setCurrentTerrainSubview(model);
            model.getCurrentHex().travelTo(model);

            println("The pegasus lets you off its back, whinnies and flies away.");
            model.getParty().randomPartyMemberSay(model,
                    List.of("Bye now, please come again soon!3",
                    "Thanks for the ride!", "Goodbye!", "That was awesome, thank you!3"));
        }
    }

    protected Point selectDirection(Model model, PegasusTravelSubView mapSubView) {
        Point selectedDir;
        do {
            print("Please select a land hex to fly to: ");
            waitForReturn();
            selectedDir = mapSubView.getCurrentPosition(model);
            if (!model.getWorld().getHex(selectedDir).canTravelTo(model)) {
                println(" That is not a valid destination.");
            }
        } while (selectedDir.x == model.getParty().getPosition().x &&
                selectedDir.y == model.getParty().getPosition().y);
        return selectedDir;
    }

    private static class PegasusTravelSubView extends MapSubView {
        private final Point cursorPos;
        private final Point startPoint;
        private boolean enabled = true;

        public PegasusTravelSubView(Model model) {
            super(model);
            cursorPos = new Point(model.getParty().getPosition());
            startPoint = new Point(model.getParty().getPosition());
        }

        @Override
        protected String getTitleText(Model model) {
            return "FLY ON PEGASUS";
        }

        @Override
        protected String getUnderText(Model model) {
            return "You are flying on a pegasus!";
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
            if (startPoint.distance(cursorPos.x+dx, cursorPos.y+dy) < 4) {
                World.move(cursorPos, dx, dy);
            }
        }

        public Point getCurrentPosition(Model model) {
            return cursorPos;
        }

        public void setAvatarEnabled(boolean b) {
            enabled = b;
        }
    }

    private class PegasusSprite extends LoopingSprite {
        public PegasusSprite() {
            super("pegasus", "enemies.png", 0x50, 32);
            setColor1(MyColors.GRAY);
            setColor2(MyColors.WHITE);
            setColor3(MyColors.PINK);
            setFrames(7);
        }
    }
}
