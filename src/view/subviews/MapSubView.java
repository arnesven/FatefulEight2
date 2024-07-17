package view.subviews;

import model.Model;
import model.SteppingMatrix;
import model.map.Direction;
import model.map.World;
import model.travellers.Traveller;
import view.BorderFrame;
import view.MyColors;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class MapSubView extends AvatarSubView {
    public static final int MAP_WIDTH_HEXES = 8;
    public static final int MAP_HEIGHT_HEXES = 10;
    public static final String TITLE_TEXT = "TRAVEL";
    private final List<Point> directions;
    private SteppingMatrix<Point> matrix;
    private boolean avatarEnabled = true;

    public MapSubView(Model model) {
        matrix = new SteppingMatrix<>(3, 3);
        directions = Direction.getDxDyDirections(model.getParty().getPosition());
        matrix.addElement(1, 1, new Point(0, 0));

        addIfOkToMoveTo(model,2, 2, directions.get(0));
        addIfOkToMoveTo(model, 1, 2, directions.get(1));
        addIfOkToMoveTo(model, 0, 2, directions.get(2));
        addIfOkToMoveTo(model, 0, 0, directions.get(3));
        addIfOkToMoveTo(model, 1, 0, directions.get(4));
        addIfOkToMoveTo(model, 2, 0, directions.get(5));

    }

    private void addIfOkToMoveTo(Model model, int col, int row, Point dxdy) {
        if (canMoveToHex(model, dxdy)) {
            matrix.addElement(col, row, dxdy);
        }
    }

    private boolean canMoveToHex(Model model, Point dxdy) {
        Point p = new Point(model.getParty().getPosition());
        p.x = p.x + dxdy.x;
        p.y = p.y + dxdy.y;
        if (model.isInCaveSystem()) {
            if (!model.getCurrentHex().getRoadInDirection(Direction.getDirectionForDxDy(model.getParty().getPosition(), dxdy))) {
                return false;
            }

        }
        return model.getWorld().canTravelTo(model, p);
    }

    @Override
    public void specificDrawArea(Model model) {
        Point cursorPos = getSelectedDestination(model);
        World worldToDraw = model.getWorld();
        if (model.isInCaveSystem()) {
            worldToDraw = model.getCaveSystem();
        }
        worldToDraw.drawYourself(model, model.getParty().getPosition(), model.getParty().getPosition(),
                    MAP_WIDTH_HEXES, MAP_HEIGHT_HEXES, Y_OFFSET, cursorPos, avatarEnabled);
        drawTravellers(model);
        drawGuide(model);
    }

    private void drawGuide(Model model) {
        if (model.getParty().getGuide() > 0) {
            BorderFrame.drawString(model.getScreenHandler(), "Guide: " + model.getParty().getGuide(),
                    X_OFFSET, Y_OFFSET-1, MyColors.WHITE, MyColors.BLACK);
        }
    }

    private void drawTravellers(Model model) {
        int count = 0;
        for (Traveller t : model.getParty().getActiveTravellers()) {
            if (count == 1) {
                BorderFrame.drawString(model.getScreenHandler(), t.getName() + ":",
                        X_OFFSET, Y_MAX, MyColors.WHITE, MyColors.BLACK);
            } else {
                BorderFrame.drawString(model.getScreenHandler(), t.getName() + ":",
                        X_MAX-t.getName().length()-3, Y_MAX, MyColors.WHITE, MyColors.BLACK);
            }
            int days = t.getRemainingDays(model);
            MyColors color = MyColors.WHITE;
            if (days < 1) {
                color = MyColors.LIGHT_RED;
            } else if (days < 6) {
                color = MyColors.YELLOW;
            }
            if (count == 1) {
                BorderFrame.drawString(model.getScreenHandler(), String.format("%2d", days),
                        X_OFFSET + t.getName().length() + 1, Y_MAX, color, MyColors.BLACK);
            } else {
                BorderFrame.drawString(model.getScreenHandler(), String.format("%2d", days),
                        X_MAX - 2, Y_MAX, color, MyColors.BLACK);
            }
            count++;
        }
    }

    protected Point getSelectedDestination(Model model) {
        Point sel = new Point(matrix.getSelectedElement());
        Point p = new Point(model.getParty().getPosition().x + sel.x,
                model.getParty().getPosition().y + sel.y);
        return p;
    }

    @Override
    protected String getUnderText(Model model) {
        if (model.isInCaveSystem()) {
            if (directions.contains(getSelectedDirection())) {
                String name = Direction.getShortNameForDxDy(model.getParty().getPosition(), getSelectedDirection());
                return "Head " + name + " through the caves.";
            }
            return "You are in the caves.";
        }
        return model.getHexInfo(getSelectedDestination(model));
    }

    @Override
    protected String getTitleText(Model model) {
        return TITLE_TEXT;
    }

    @Override
    public boolean handleKeyEvent(KeyEvent keyEvent, Model model) {
        return matrix.handleKeyEvent(keyEvent);
    }

    public Point getSelectedDirection() {
        return matrix.getSelectedElement();
    }

    public void drawAvatarEnabled(boolean b) {
        this.avatarEnabled = b;
    }

    public List<Point> getDirections(Model model) {
        return directions;
    }
}
