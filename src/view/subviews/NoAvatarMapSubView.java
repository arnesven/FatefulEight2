package view.subviews;

import model.Model;
import model.map.World;

import java.awt.*;

public class NoAvatarMapSubView extends SubView {
    private final Point position;
    private final Point viewPoint;
    private final int ySize;

    public NoAvatarMapSubView(Point position, int ySize) {
        this.position = position;
        this.viewPoint = new Point(position.x, position.y + 3);
        this.ySize = ySize;
    }

    @Override
    protected void drawArea(Model model) {
        World worldToDraw = model.getWorld();
        worldToDraw.drawYourself(model, viewPoint, position, 8, ySize,
                Y_OFFSET, position, false);
    }

    @Override
    protected String getUnderText(Model model) {
        return "";
    }

    @Override
    protected String getTitleText(Model model) {
        return "";
    }
}
